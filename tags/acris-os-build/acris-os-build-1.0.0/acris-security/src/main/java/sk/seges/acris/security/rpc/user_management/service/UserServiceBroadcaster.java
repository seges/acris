package sk.seges.acris.security.rpc.user_management.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.security.GrantedAuthority;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.core.client.component.semaphore.Semaphore;
import sk.seges.acris.core.client.component.semaphore.SemaphoreEvent;
import sk.seges.acris.core.client.component.semaphore.SemaphoreListener;
import sk.seges.acris.security.rpc.exception.ServerException;
import sk.seges.acris.security.rpc.session.ClientSession;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.acris.security.rpc.user_management.domain.LoginToken;

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
 * service determines the specific instance of {@link GenericUser} to be
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
	private String primaryEntryPoint;
	private ClientSession clientSession;

	public void setClientSession(ClientSession clientSession) {
		this.clientSession = clientSession;
	}

	public void addUserService(IUserServiceAsync userService) {
		ServiceDefTarget subscriptionEndpoint = (ServiceDefTarget) userService;
		String entryPoint = subscriptionEndpoint.getServiceEntryPoint();

		if (entryPoint == null || entryPoint.length() == 0) {
			throw new IllegalArgumentException(
					"Unable to register non initialized service. Please set entry service point before registering.");
		}

		userServices.put(entryPoint, userService);
	}

	public void setPrimaryEntryPoint(String primaryEntryPoint) {
		this.primaryEntryPoint = primaryEntryPoint;
	}

	@Override
	public void getLoggedUser(AsyncCallback<GenericUser> callback) {
		throw new IllegalArgumentException(
				"Not supported method in broadcast mode. Please, call method separatelly on specific user service.");
	}

	@Override
	public void login(final LoginToken token, final AsyncCallback<ClientSession> callback) {
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
						String resolvedPrimaryEntryPoint = (primaryEntryPoint != null ? primaryEntryPoint
								: (clientSession != null && clientSession.get(PRIMARY_ENTRY_POINT) != null ? (String) clientSession
										.get(PRIMARY_ENTRY_POINT)
										: null));
						if (resolvedPrimaryEntryPoint != null) {
							// merge authorities from all services to one set
							ClientSession primaryResult = successes.get(resolvedPrimaryEntryPoint);
							GenericUser user = primaryResult.getUser();
							Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
							add(user.getAuthorities(), authorities);

							for (Entry<String, ClientSession> entry : successes.entrySet()) {
								if (!resolvedPrimaryEntryPoint.equals(entry.getKey())) {
									add(entry.getValue().getUser().getAuthorities(), authorities);
								}
							}
							user.setAuthorities(createArray(authorities));
							callback.onSuccess(primaryResult);
						} else {
							callback.onSuccess(successes.values().iterator().next());
						}
					}
				}
			}

			private void add(GrantedAuthority[] userAuthorities, Set<GrantedAuthority> allAuthorities) {
				for (GrantedAuthority authority : userAuthorities) {
					allAuthorities.add(authority);
				}
			}

			private GrantedAuthority[] createArray(Set<GrantedAuthority> authorities) {
				GrantedAuthority[] array = new GrantedAuthority[authorities.size()];
				int i = 0;
				for (GrantedAuthority authority : authorities) {
					array[i++] = authority;
				}
				return array;
			}
		});

		signalLoginServices(token, semaphore, failures, successes);
	}

	private void signalLoginServices(final LoginToken token, final Semaphore semaphore,
			final List<Throwable> failures, final Map<String, ClientSession> successes) {
		for (final Entry<String, IUserServiceAsync> userServiceEntry : userServices.entrySet()) {
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

	public static class BroadcastingException extends ServerException {
		private static final long serialVersionUID = 6497048424054217121L;

		private Throwable[] causes;

		public BroadcastingException(Throwable[] causes) {
			super("Unable to login in all user services");
			this.causes = causes;
		}

		public Throwable[] getCauses() {
			return causes;
		}
	}
}