package sk.seges.acris.security.shared.acl.service;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IRemoteAclMaintenanceServiceAsync {

	public void removeACLEntries(UserData<?> user, String[] securedClassNames, AsyncCallback<Void> callback);
	public void removeACLEntries(List<Long> aclIds, String className, UserData<?> user, AsyncCallback<Void> callback);
	
	public void resetACLEntries(String className, Long aclId, UserData<?> user, Permission[] authorities, AsyncCallback<Void> callback);
	public void resetACLEntriesLoggedRole(String className, Long aclId, Permission[] permissions, AsyncCallback<Void> callback);
}
