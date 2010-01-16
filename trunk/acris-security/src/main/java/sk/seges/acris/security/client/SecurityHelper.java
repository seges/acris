package sk.seges.acris.security.client;

import sk.seges.acris.security.rpc.domain.IUserPermission;

public class SecurityHelper {

	public static void setPanelRole(IUserPermission permission, Object rsp){
		setPanelPermissions(new IUserPermission[] {permission}, rsp);
	}
	
	public static void setPanelPermissions(IUserPermission[] permissions, Object rsp){
		if (rsp instanceof IRuntimeRolesProvider) {
			((IRuntimeRolesProvider)rsp).setPermissions(permissions); //role=ROLE_USER
		}
	}
}
