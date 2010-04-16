package sk.seges.acris.security.client.mediator;

import sk.seges.acris.security.rpc.user_management.domain.IUserPermission;


public interface IRuntimePermissionMediator {

	void setPermission(String role);
    void setPermissions(String[] roles);

    void setPermission(IUserPermission role);
	void setPermissions(IUserPermission[] roles);
	
	String[] getPermissions();
}