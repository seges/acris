package sk.seges.acris.security.shared.acl.service;

import java.util.List;
import java.util.Map;

import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import com.google.gwt.user.client.rpc.RemoteService;

@RemoteServiceDefinition
public interface IAclMaintenanceServiceRemote extends RemoteService {
	
	public void removeACLEntries(GenericUserDTO user, String[] securedClassNames);
	public void removeACLEntries(List<Long> aclIds, String className, GenericUserDTO user);
	
	public void resetACLEntries(String className, Long aclId, GenericUserDTO user, Permission[] authorities);
	public void resetACLEntriesLoggedRole(String className, Long aclId, Permission[] authorities);
	
	void setAclEntries(String className, Long aclId, GenericUserDTO user,Permission[] authorities);
	void setAclEntries(Map<String, List<Long>> acls, GenericUserDTO user,Permission[] authorities);
}
