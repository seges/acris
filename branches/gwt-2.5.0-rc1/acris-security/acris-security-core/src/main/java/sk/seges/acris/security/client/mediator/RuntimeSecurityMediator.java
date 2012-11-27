package sk.seges.acris.security.client.mediator;

import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;

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
	public static void setRuntimePermission(UserPermission permission, Object runtimeSecuredObject) {
		setRuntimePermissions(new UserPermission[] { permission }, runtimeSecuredObject);
	}

	public static void setGrants(String[] grants, Object runtimeSecuredObject) {
		if (runtimeSecuredObject instanceof IRuntimeAuthorityMediator) {
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
	public static void setRuntimePermissions(UserPermission[] permissions, Object runtimeSecuredObject) {
		if (runtimeSecuredObject instanceof IRuntimeAuthorityMediator) {
			((IRuntimeAuthorityMediator) runtimeSecuredObject).setPermissions(permissions);
		}
	}
}
