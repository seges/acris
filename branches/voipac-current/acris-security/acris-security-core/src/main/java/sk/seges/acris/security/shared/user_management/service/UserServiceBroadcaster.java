package sk.seges.acris.security.shared.user_management.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.common.util.Pair;
import sk.seges.acris.core.client.component.semaphore.Semaphore;
import sk.seges.acris.core.client.component.semaphore.SemaphoreEvent;
import sk.seges.acris.core.client.component.semaphore.SemaphoreListener;
import sk.seges.acris.security.client.session.SessionServiceDefTarget;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * User service broadcaster is responsible for broadcasting login information
 * from client to all participating user services on server. It ensures that
 * security context for the user will be presented in all servlet context where
 * user services resides.
 * <p>
 * Broadcasting is useful when there are more servlet contexts deployed (e.g.
 * multiple WARs deployed in Tomcat) and each context contains custom user
 * service. These contexts compose the server part of your enterprise
 * application. Because of the fact they are functionally different they are
 * divided to separate distribution packages (WARs). They share (only or not
 * only) the user information and the client part.
 * </p>
 * <p>
 * Thus when a user logs in on the client login information (using
 * {@link LoginToken}) must be propagated to all user services in all different
 * contexts.
 * </p>
 * <p>
 * Example: When HTTP session is used to store login information on the server,
 * each context must have the security context stored in the session.
 * </p>
 * <p>
 * There is usually one user service that is the most important (primary). This
 * service determines the specific instance of {@link GenericUserDTO} to be
 * returned to the client. So there are several user services but only one
 * instance of {@link ClientSession} will be returned. If you specify primary
 * entry point (using {@link #setPrimaryEntryPoint(String)} or setting key
 * {@link #PRIMARY_ENTRY_POINT} in {@link ClientSession}) you also specify which
 * user service is the primary one. Into its user information all authorities
 * from all user services will be merged.
 * </p>
 * <p>
 * Setting primary entry point through ClientSession's key allows you to
 * decouple and postpone the setting of the entry point until it is needed.
 * Using {@link #setPrimaryEntryPoint(String)} forces you initialize the
 * broadcaster (and user services) in the time of setting - it can break e.g.
 * correct code splitting.
 * </p>
 * 
 * @author fat
 * @author ladislav.gazo
 */
public class UserServiceBroadcaster implements IUserServiceAsync {

	public static final String PRIMARY_ENTRY_POINT = "primaryEntryPoint";

	private Map<String, IUserServiceAsync> userServices = new HashMap<String, IUserServiceAsync>();
	private ClientSession clientSession;
	private String primaryEntryPoint;

	public void addUserService(IUserServiceAsync userService) {
		ServiceDefTarget subscriptionEndpoint = (ServiceDefTarget) userService;
		String entryPoint = subscriptionEndpoint.getServiceEntryPoint();

		if (entryPoint == null || entryPoint.length() == 0) {
			throw new IllegalArgumentException(
					"Unable to register non initialized service. Please set entry service point before registering.");
		}

		userServices.put(entryPoint, userService);
	}

	public void setClientSession(ClientSession clientSession) {
		this.clientSession = clientSession;
	}

	public void setPrimaryEntryPoint(String primaryEntryPoint) {
		this.primaryEntryPoint = primaryEntryPoint;
	}

	private String resolvePrimaryEntryPoint() {
		return (primaryEntryPoint != null ? primaryEntryPoint : (clientSession != null
				&& clientSession.get(PRIMARY_ENTRY_POINT) != null ? (String) clientSession.get(PRIMARY_ENTRY_POINT)
				: null));
	}

	@Override
	public void authenticate(LoginToken token, final AsyncCallback<String> callback) throws ServerException {
		authenticatedLogin(token, callback, null);
	}

	@Override
	public void login(final LoginToken token, final AsyncCallback<ClientSession> callback) throws ServerException {
		authenticatedLogin(token, null, callback);
	}

	private void authenticatedLogin(final LoginToken token, final AsyncCallback<String> stringCallback,
			final AsyncCallback<ClientSession> clientCallback) {
		final int count = userServices.size();

		if (count == 0) {
			throw new IllegalArgumentException(
					"No user service was registered. Please register user service in order to execute this method.");
		}

		final List<Throwable> failures = new ArrayList<Throwable>();
		final Map<String, ClientSession> successes = new HashMap<String, ClientSession>();

		final Semaphore semaphore = new Semaphore(2);
		semaphore.raise(1);

		semaphore.addListener(new SemaphoreListener() {

			@Override
			public void change(SemaphoreEvent event) {
				if (event.getCount() == event.getStates()[0] + event.getStates()[1]) {
					if (event.getStates()[1] > 0) {
						Throwable[] throwables = new Throwable[failures.size()];
						failures.toArray(throwables);

						if (stringCallback != null) {
							stringCallback.onFailure(new BroadcastingException(throwables));
						} else {
							clientCallback.onFailure(new BroadcastingException(throwables));
						}
					} else {
						String resolvedPrimaryEntryPoint = resolvePrimaryEntryPoint();
						if (resolvedPrimaryEntryPoint != null) {
							ClientSession primaryResult = successes.get(resolvedPrimaryEntryPoint);
							if (stringCallback != null) {
								stringCallback.onSuccess(primaryResult.getSessionId());
							} else {
								// merge authorities from all services to one
								// set
								UserData<?> user = primaryResult.getUser();

								if (user == null) {
									clientCallback.onFailure(new BroadcastingException("User is null"));
								}

								List<String> authorities = new ArrayList<String>();
								add(user.getUserAuthorities(), authorities);

								for (Entry<String, ClientSession> entry : successes.entrySet()) {
									if (!resolvedPrimaryEntryPoint.equals(entry.getKey())) {
										add(entry.getValue().getUser().getUserAuthorities(), authorities);
										primaryResult.merge(entry.getValue());
									}
								}
								user.setUserAuthorities(authorities);
								clientCallback.onSuccess(primaryResult);
							}
						} else {
							if (stringCallback != null) {
								stringCallback.onSuccess(successes.values().iterator().next().getSessionId());
							} else {
								clientCallback.onSuccess(successes.values().iterator().next());
							}
						}
					}
				}
			}

			private <T> void add(List<T> userAuthorities, List<T> allAuthorities) {
				for (T authority : userAuthorities) {
					allAuthorities.add(authority);
				}
			}
		});

		signalLoginServices(token, semaphore, failures, successes, stringCallback != null ? true : false);
	}

	private Pair<String, IUserServiceAsync> resolvePrimaryService() {
		if (userServices.size() == 0) {
			return null;
		}
		String primaryEntryPoint = resolvePrimaryEntryPoint();

		if (primaryEntryPoint == null) {
			Entry<String, IUserServiceAsync> primaryEntry = userServices.entrySet().iterator().next();
			return new Pair<String, IUserServiceAsync>(primaryEntry.getKey(), primaryEntry.getValue());
		}
		for (final Entry<String, IUserServiceAsync> userServiceEntry : userServices.entrySet()) {
			if (userServiceEntry.getKey().equals(primaryEntryPoint)) {
				return new Pair<String, IUserServiceAsync>(userServiceEntry.getKey(), userServiceEntry.getValue());
			}
		}

		Entry<String, IUserServiceAsync> primaryEntry = userServices.entrySet().iterator().next();
		return new Pair<String, IUserServiceAsync>(primaryEntry.getKey(), primaryEntry.getValue());
	}

	private void signalLoginServices(final LoginToken token, final Semaphore semaphore, final List<Throwable> failures,
			final Map<String, ClientSession> successes, final boolean authentication) {
		final Pair<String, IUserServiceAsync> primaryServicePair = resolvePrimaryService();

		if (authentication) {
			primaryServicePair.getSecond().authenticate(token, new TrackingAsyncCallback<String>() {

				@Override
				public void onFailureCallback(Throwable cause) {
					failures.add(cause);
					semaphore.signal(1);
				}

				@Override
				public void onSuccessCallback(String result) {
					semaphore.raise(userServices.size() - 1);

					ClientSession resultSession = new ClientSession();
					resultSession.setSessionId(result);
					successes.put(primaryServicePair.getFirst(), resultSession);
					semaphore.signal(0);

					final String sessionId = result;

					for (final Entry<String, IUserServiceAsync> userServiceEntry : userServices.entrySet()) {
						if (!userServiceEntry.getKey().equals(primaryServicePair.getFirst())) {
							if (userServiceEntry.getValue() instanceof SessionServiceDefTarget) {
								ClientSession session = ((SessionServiceDefTarget) userServiceEntry.getValue())
										.getSession();
								if (session != null) {
									session.setSessionId(sessionId);
								}
							}

							userServiceEntry.getValue().authenticate(token, new TrackingAsyncCallback<String>() {

								@Override
								public void onFailureCallback(Throwable cause) {
									failures.add(cause);
									semaphore.signal(1);
								}

								@Override
								public void onSuccessCallback(String result) {
									ClientSession resultSession = new ClientSession();
									resultSession.setSessionId(result);
									successes.put(userServiceEntry.getKey(), resultSession);
									semaphore.signal(0);
								}
							});
						}
					}
				}
			});
		} else {
			primaryServicePair.getSecond().login(token, new TrackingAsyncCallback<ClientSession>() {

				@Override
				public void onFailureCallback(Throwable cause) {
					failures.add(cause);
					semaphore.signal(1);
				}

				@Override
				public void onSuccessCallback(ClientSession result) {
					successes.put(primaryServicePair.getFirst(), result);
					semaphore.signal(0);

					final String sessionId = result.getSessionId();

					for (final Entry<String, IUserServiceAsync> userServiceEntry : userServices.entrySet()) {
						if (!userServiceEntry.getKey().equals(primaryServicePair.getFirst())) {
							if (userServiceEntry.getValue() instanceof SessionServiceDefTarget) {
								ClientSession session = ((SessionServiceDefTarget) userServiceEntry.getValue())
										.getSession();
								if (session != null) {
									session.setSessionId(sessionId);
								}
							}

							userServiceEntry.getValue().login(token, new TrackingAsyncCallback<ClientSession>() {
								@Override
								public void onFailureCallback(Throwable cause) {
									failures.add(cause);
									semaphore.signal(1);
								}

								@Override
								public void onSuccessCallback(ClientSession result) {
									successes.put(userServiceEntry.getKey(), result);
									semaphore.signal(0);
								}
							});
						}
					}
				}
			});
		}
	}

	private void signalLogoutServices(final Semaphore semaphore, final List<Throwable> failures,
			final List<String> successes) {
		for (final Entry<String, IUserServiceAsync> userServiceEntry : userServices.entrySet()) {
			userServiceEntry.getValue().logout(new TrackingAsyncCallback<Void>() {
				@Override
				public void onFailureCallback(Throwable cause) {
					failures.add(cause);
					semaphore.signal(1);
				}

				@Override
				public void onSuccessCallback(Void result) {
					successes.add(userServiceEntry.getKey());
					semaphore.signal(0);
				}
			});
		}
	}

	@Override
	public void logout(final AsyncCallback<Void> callback) {
		int count = userServices.size();

		if (count == 0) {
			throw new IllegalArgumentException(
					"No user service was registered. Please register user service in order to execute this method.");
		}

		final List<Throwable> failures = new ArrayList<Throwable>();
		final List<String> successes = new ArrayList<String>();

		final Semaphore semaphore = new Semaphore(2);
		semaphore.raise(count);

		semaphore.addListener(new SemaphoreListener() {
			@Override
			public void change(SemaphoreEvent event) {
				if (event.getCount() == event.getStates()[0] + event.getStates()[1]) {
					if (event.getStates()[1] > 0) {
						Throwable[] throwables = new Throwable[failures.size()];
						failures.toArray(throwables);
						callback.onFailure(new BroadcastingException(throwables));
					} else {
						callback.onSuccess(null);
					}
				}
			}
		});

		signalLogoutServices(semaphore, failures, successes);
	}

	@Override
	public void getLoggedSession(UserContext userContext, final AsyncCallback<ClientSession> callback) throws ServerException {
		final int count = userServices.size();

		if (count == 0) {
			throw new IllegalArgumentException(
					"No user service was registered. Please register user service in order to execute this method.");
		}

		final List<Throwable> failures = new ArrayList<Throwable>();
		final Map<String, ClientSession> successes = new HashMap<String, ClientSession>();

		final Semaphore semaphore = new Semaphore(2);
		semaphore.raise(count);

		semaphore.addListener(new SemaphoreListener() {

			@Override
			public void change(SemaphoreEvent event) {
				if (event.getCount() == event.getStates()[0] + event.getStates()[1]) {
					if (event.getStates()[1] > 0) {
						Throwable[] throwables = new Throwable[failures.size()];
						failures.toArray(throwables);
						callback.onFailure(new BroadcastingException(throwables));
					} else {
						String resolvedPrimaryEntryPoint = resolvePrimaryEntryPoint();
						if (resolvedPrimaryEntryPoint != null) {
							// merge authorities from all services to one set
							ClientSession primaryResult = successes.get(resolvedPrimaryEntryPoint);
							UserData<?> user = primaryResult.getUser();
							if (!checkUser(user, callback)) {
								return;
							}

							List<String> authorities = new ArrayList<String>();
							add(user.getUserAuthorities(), authorities);

							for (Entry<String, ClientSession> entry : successes.entrySet()) {
								if (!resolvedPrimaryEntryPoint.equals(entry.getKey())) {
									UserData<?> entryUser = entry.getValue().getUser();
									if (entryUser != null /*&& entryUser.getUserAuthorities() != null*/) {
										if (entryUser.getUserAuthorities() != null) add(entryUser.getUserAuthorities(), authorities);
										primaryResult.merge(entry.getValue());
									} else {
										callback.onFailure(new BroadcastingException("User is null in " + entry.getKey()));
									}
								}
							}
							user.setUserAuthorities(authorities);
							callback.onSuccess(primaryResult);
						} else {
							UserData<?> user = successes.values().iterator().next().getUser();
							if (!checkUser(user, callback)) {
								return;
							}

							callback.onSuccess(successes.values().iterator().next());
						}
					}
				}
			}

			private boolean checkUser(UserData<?> user, AsyncCallback<ClientSession> callback) {
				if (user == null) {
					callback.onFailure(new BroadcastingException("User is null"));
					return false;
				}
				return true;
			}

			private <T> void add(List<T> userAuthorities, List<T> allAuthorities) {
				for (T authority : userAuthorities) {
					allAuthorities.add(authority);
				}
			}
		});

		signalUserServices(semaphore, failures, successes, userContext);
	}

	@Override
	public void getLoggedUserName(UserContext userContext, final AsyncCallback<String> callback) throws ServerException {
		final int count = userServices.size();

		if (count == 0) {
			throw new IllegalArgumentException(
					"No user service was registered. Please register user service in order to execute this method.");
		}

		Pair<String, IUserServiceAsync> primaryService = resolvePrimaryService();

		primaryService.getSecond().getLoggedUserName(userContext, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(String result) {
				callback.onSuccess(result);
			}
		});
	}

	private void signalUserServices(final Semaphore semaphore, final List<Throwable> failures,
			final Map<String, ClientSession> successes, UserContext userContext) {
		for (final Entry<String, IUserServiceAsync> userServiceEntry : userServices.entrySet()) {

			userServiceEntry.getValue().getLoggedSession(userContext, new AsyncCallback<ClientSession>() {

				@Override
				public void onFailure(Throwable caught) {
					failures.add(caught);
					semaphore.signal(1);
				}

				@Override
				public void onSuccess(ClientSession result) {
					ClientSession session = new ClientSession();
					if (result != null) {
						session.setUser(result.getUser());
						session.merge(result);
					}
					successes.put(userServiceEntry.getKey(), session);
					semaphore.signal(0);
				}
			});
		}
	}

	public static class BroadcastingException extends ServerException {
		private static final long serialVersionUID = 6497048424054217121L;

		private Throwable[] causes;

		public BroadcastingException(String message) {
			super(message);
		}

		public BroadcastingException(Throwable[] causes) {
			super("Unable to login in all user services");
			this.causes = causes;
		}

		public Throwable[] getCauses() {
			return causes;
		}
	}

	@Override
	public void changeAuthentication(ClientSession clientSession, AsyncCallback<ClientSession> callback) {
		
		
	}
}
