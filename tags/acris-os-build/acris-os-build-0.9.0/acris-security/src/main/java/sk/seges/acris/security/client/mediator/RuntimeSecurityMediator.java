package sk.seges.acris.security.client.mediator;

import sk.seges.acris.security.rpc.user_management.domain.IUserPermission;

public class RuntimeSecurityMediator {

	public static void setRuntimePermission(String permission, Object runtimeSecuredObject){
		setRuntimePermissions(new String[] {permission}, runtimeSecuredObject);
	}

	public static void setRuntimePermission(IUserPermission permission, Object runtimeSecuredObject){
		setRuntimePermissions(new IUserPermission[] {permission}, runtimeSecuredObject);
	}

	public static void setRuntimePermissions(String[] permissions, Object runtimeSecuredObject){
		if (runtimeSecuredObject instanceof IRuntimePermissionMediator) {
			((IRuntimePermissionMediator)runtimeSecuredObject).setPermissions(permissions);
		}
	}

	public static void setRuntimePermissions(IUserPermission[] permissions, Object runtimeSecuredObject){
		if (runtimeSecuredObject instanceof IRuntimePermissionMediator) {
			((IRuntimePermissionMediator)runtimeSecuredObject).setPermissions(permissions);
		}
	}
}
