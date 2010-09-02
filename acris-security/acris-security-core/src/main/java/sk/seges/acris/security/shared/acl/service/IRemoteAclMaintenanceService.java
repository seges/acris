package sk.seges.acris.security.shared.acl.service;

import java.util.List;

import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IRemoteAclMaintenanceService extends RemoteService {

	public void setACLEntries(List<? extends ISecuredObject> securedObjects, UserData user);

	public void setACLEntries(List<? extends ISecuredObject> securedObjects, UserData user, Permission[] authorities);

	public void removeACLEntries(UserData user, String[] securedClassNames);

	public void removeACLEntries(List<? extends ISecuredObject> securedObject, UserData user);
}
