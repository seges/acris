package sk.seges.acris.security.client;

import sk.seges.acris.security.rpc.domain.IUserPermission;


public interface IRuntimePermissionProvider {

	void setPermission(String role);
    void setPermissions(String[] roles);

    void setPermission(IUserPermission role);
	void setPermissions(IUserPermission[] roles);
	
	String[] getPermissions();
}