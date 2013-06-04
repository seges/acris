package sk.seges.acris.security.shared.user_management.service;

import java.util.List;
import java.util.Map;

import sk.seges.acris.core.client.annotation.RemoteServicePath;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;

import com.google.gwt.user.client.rpc.RemoteService;

@RemoteServicePath(value = "openID")
public interface IOpenIDUserRemoteService extends RemoteService {

	GenericUserDTO getUserByOpenIDIdentifier(String identifier);

	List<OpenIDProvider> findProvidersByUserName(String userName);

	void saveUserByIdentifiers(String userName, Map<String, Object> identifiers);
}
