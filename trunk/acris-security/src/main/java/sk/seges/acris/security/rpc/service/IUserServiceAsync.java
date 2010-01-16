package sk.seges.acris.security.rpc.service;

import sk.seges.acris.security.rpc.domain.GenericUser;
import sk.seges.acris.security.rpc.to.ClientContext;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IUserServiceAsync {
//	void login(String username, String password, String language, IUserDetail userDetail, 
//			AsyncCallback<ClientContext> callback);

	void login(String username, String password, String language, 
			AsyncCallback<ClientContext> callback);

	void getLoggedUser(AsyncCallback<GenericUser> callback);

	void logout(AsyncCallback<Void> callback);
	
//	void getAuditTrailedLoggedUsernames(AsyncCallback<List<String>> callback);

}
