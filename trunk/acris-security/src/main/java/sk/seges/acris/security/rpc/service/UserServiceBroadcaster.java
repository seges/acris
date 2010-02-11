package sk.seges.acris.security.rpc.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.security.rpc.domain.GenericUser;
import sk.seges.acris.security.rpc.session.ClientSession;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class UserServiceBroadcaster implements IUserServiceAsync {
	
	class CountHolder {
		int count;
	}
	
	private Map<String, IUserServiceAsync> userServices = new HashMap<String, IUserServiceAsync>();
	
	public void addUserService(IUserServiceAsync userService) {
		ServiceDefTarget subscriptionEndpoint = (ServiceDefTarget) userService;
		String entryPoint = subscriptionEndpoint.getServiceEntryPoint();
		
		if (entryPoint == null || entryPoint.length() == 0) {
			throw new IllegalArgumentException("Unable to register non initialized service. Please set entry service point before registering.");
		}

		userServices.put(entryPoint, userService);
	}

	@Override
	public void getLoggedUser(AsyncCallback<GenericUser> callback) {
		throw new IllegalArgumentException("Not supported method in broadcast mode. Please, call method separatelly on specific user service.");
	}

	@Override
	public void login(final String username, final String password, final String language,
			final AsyncCallback<ClientSession> callback) {
		int count = userServices.size();
		
		final CountHolder countHolder = new CountHolder();
		countHolder.count = count;
		
		if (count == 0) {
			throw new IllegalArgumentException("No user service was registered. Please register user service in order to execute this method.");
		}

		final Iterator<IUserServiceAsync> userServiceIterator = userServices.values().iterator();
		
		userServiceIterator.next().login(username, password, language, new TrackingAsyncCallback<ClientSession>() {

				@Override
				public void onFailureCallback(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccessCallback(ClientSession result) {
					countHolder.count--;
					resendLoginRequest(username, password, language, callback, userServiceIterator, countHolder, result);
				}
			});
	}
	
	private void notifyUser(final AsyncCallback<ClientSession> callback, CountHolder countHolder, ClientSession result) {
		if (countHolder.count == 0) {
			callback.onSuccess(result);
		}
	}
	
	private void resendLoginRequest(final String username, final String password, final String language,
			final AsyncCallback<ClientSession> callback, Iterator<IUserServiceAsync> userServiceIterator, 
			final CountHolder countHolder, final ClientSession result) {

		while(userServiceIterator.hasNext()) {
			userServiceIterator.next().login(username, password, language, new TrackingAsyncCallback<ClientSession>() {
	
					@Override
					public void onFailureCallback(Throwable caught) {
						callback.onFailure(caught);
					}
	
					@Override
					public void onSuccessCallback(ClientSession result) {
						countHolder.count--;
						notifyUser(callback, countHolder, result);
					}
				});
		}
	}

	@Override
	public void logout(final AsyncCallback<Void> callback) {
		int count = userServices.size();
		
		final CountHolder countHolder = new CountHolder();
		countHolder.count = count;
		
		if (count == 0) {
			throw new IllegalArgumentException("No user service was registered. Please register user service in order to execute this method.");
		}

		final Iterator<IUserServiceAsync> userServiceIterator = userServices.values().iterator();
		
		while(userServiceIterator.hasNext()) {
			userServiceIterator.next().logout(new TrackingAsyncCallback<Void>() {

				@Override
				public void onFailureCallback(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccessCallback(Void result) {
					countHolder.count--;
					if (countHolder.count == 0) {
						callback.onSuccess(result);
					}
				}
			});
		}
	}
}