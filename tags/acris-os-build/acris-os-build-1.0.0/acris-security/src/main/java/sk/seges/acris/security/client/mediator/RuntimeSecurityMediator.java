package sk.seges.acris.security.client.mediator;

import sk.seges.acris.security.client.IRuntimeSecuredObject;
import sk.seges.acris.security.rpc.user_management.domain.IUserPermission;

/**
 * Mediates the possibility to set authorities in runtime to the implementation
 * generated for runtime secured object.
 * 
 * @author fat
 */
public class RuntimeSecurityMediator {

	public static void setGrant(String grant, Object runtimeSecuredObject) {
		setGrants(new String[] { grant }, runtimeSecuredObject);
	}

	/**
	 * @deprecated Use strings as grants or another (custom) mediator.
	 * 
	 * @param permission
	 * @param runtimeSecuredObject
	 */
	@Deprecated
	public static void setRuntimePermission(IUserPermission permission, Object runtimeSecuredObject) {
		setRuntimePermissions(new IUserPermission[] { permission }, runtimeSecuredObject);
	}

	public static void setGrants(String[] grants, Object runtimeSecuredObject) {
		if (runtimeSecuredObject instanceof IRuntimeSecuredObject) {
			((IRuntimeAuthorityMediator) runtimeSecuredObject).setGrants(grants);
		}
	}

	/**
	 * @deprecated Use strings as grants or another (custom) mediator.
	 * 
	 * @param permissions
	 * @param runtimeSecuredObject
	 */
	@Deprecated
	public static void setRuntimePermissions(IUserPermission[] permissions, Object runtimeSecuredObject) {
		if (runtimeSecuredObject instanceof IRuntimeSecuredObject) {
			((IRuntimeAuthorityMediator) runtimeSecuredObject).setPermissions(permissions);
		}
	}
}
