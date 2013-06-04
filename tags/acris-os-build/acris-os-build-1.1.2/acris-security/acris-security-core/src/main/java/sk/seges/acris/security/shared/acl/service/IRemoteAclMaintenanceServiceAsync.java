package sk.seges.acris.security.shared.acl.service;

import java.util.List;

import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IRemoteAclMaintenanceServiceAsync {

	public void setACLEntries(List<ISecuredObject> securedObjects, UserData user, AsyncCallback<Void> callback);

	public void setACLEntries(List<ISecuredObject> securedObjects, UserData user, Permission[] authorities, AsyncCallback<Void> callback);

	public void removeACLEntries(UserData user, String[] securedClassNames, AsyncCallback<Void> callback);

	public void removeACLEntries(List<? extends ISecuredObject> securedObjects, UserData user, AsyncCallback<Void> callback);
}
