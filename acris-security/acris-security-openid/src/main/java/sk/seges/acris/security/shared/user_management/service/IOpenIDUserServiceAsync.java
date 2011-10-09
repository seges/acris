package sk.seges.acris.security.shared.user_management.service;

import java.util.List;
import java.util.Map;

import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IOpenIDUserServiceAsync {

	void getUserByOpenIDIdentifier(String identifier, AsyncCallback<UserData<?>> callback);

	void findProvidersByUserName(String userName, AsyncCallback<List<OpenIDProvider>> callback);

	void saveUserByIdentifiers(String userName, Map<String, Object> identifiers, AsyncCallback<Void> callback);
}
