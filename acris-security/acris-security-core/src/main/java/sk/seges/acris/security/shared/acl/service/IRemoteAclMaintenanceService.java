package sk.seges.acris.security.shared.acl.service;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IRemoteAclMaintenanceService extends RemoteService {
	
	public void removeACLEntries(UserData user, String[] securedClassNames);

	public void removeACLEntries(List<Long> aclIds, String className, UserData user);
	
	public void resetACLEntries(Long aclId, UserData user, Permission[] authorities);

	public void resetACLEntriesLoggedRole(Long aclId, Permission[] permissions);
}
