package sk.seges.acris.security.shared.user_management.service;

import java.util.List;
import java.util.Map;

import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IOpenIDUserService extends RemoteService {

	UserData<?> getUserByOpenIDIdentifier(String identifier);

	List<OpenIDProvider> findProvidersByUserName(String userName);

	void saveUserByIdentifiers(UserData<?> user, Map<String, Object> identifiers);
}
