package sk.seges.acris.security.shared.acl.service;

import java.util.List;
import java.util.Map;

import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;
import sk.seges.sesam.security.shared.model.api.ClientSecuredEntity;
import sk.seges.sesam.shared.domain.api.HasId;

import com.google.gwt.user.client.rpc.RemoteService;

@RemoteServiceDefinition
public interface IAclMaintenanceServiceRemote extends RemoteService {
	
	void removeACLEntries(GenericUserDTO user, String[] securedClassNames);

	<T extends HasId<?>> List<ClientSecuredEntity<T>> fetchAclSecurityData(List<T> dtos);
	<T extends HasId<?>> ClientSecuredEntity<T> fetchAclSecurityData(T dto);
	
	void resetACLEntries(ClientSecuredEntity<?> securedObject, GenericUserDTO user, Permission[] authorities);
	void resetACLEntriesLoggedRole(ClientSecuredEntity<?> securedObject, Permission[] authorities);

	void setAclEntries(ClientSecuredEntity<?> securedObject, GenericUserDTO user,Permission[] authorities);
	void setAclEntries(Map<String, List<Long>> acls, GenericUserDTO user,Permission[] authorities);
}
