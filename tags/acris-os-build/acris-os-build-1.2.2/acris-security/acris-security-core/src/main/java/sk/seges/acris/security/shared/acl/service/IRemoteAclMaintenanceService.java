package sk.seges.acris.security.shared.acl.service;

import java.util.List;
import java.util.Map;

import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IRemoteAclMaintenanceService extends RemoteService {
	
	public void removeACLEntries(UserData user, String[] securedClassNames);
	public void removeACLEntries(List<Long> aclIds, String className, UserData user);
	
	public void resetACLEntries(String className, Long aclId, UserData user, Permission[] authorities);
	public void resetACLEntriesLoggedRole(String className, Long aclId, Permission[] authorities);
	
	void setAclEntries(String className, Long aclId, UserData user,Permission[] authorities);
	void setAclEntries(Map<String, List<Long>> acls, UserData user,Permission[] authorities);
}
