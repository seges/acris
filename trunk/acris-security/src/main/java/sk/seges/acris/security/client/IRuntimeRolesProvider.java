package sk.seges.acris.security.client;

import sk.seges.acris.security.rpc.domain.IUserPermission;


/**
 * class which implements this interface is extended and generated
 * by com.saf.cs.security.rebind.RuntimeSecuredPanelGenerator
 *
 */
public interface IRuntimeRolesProvider {

	void setPermission(IUserPermission role);
	void setPermissions(IUserPermission[] roles);
	String[] getRoles();
}
