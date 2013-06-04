package sk.seges.acris.security.client.mediator;

import sk.seges.acris.security.rpc.user_management.domain.IUserPermission;

public interface IRuntimeAuthorityMediator {

	void setGrant(String grant);
    void setGrants(String[] grants);

    @Deprecated
    void setPermission(IUserPermission role);
    @Deprecated
	void setPermissions(IUserPermission[] roles);
	
	String[] getGrants();
}