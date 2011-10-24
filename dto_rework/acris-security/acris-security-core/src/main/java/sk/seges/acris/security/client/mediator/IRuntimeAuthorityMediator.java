package sk.seges.acris.security.client.mediator;

import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;

public interface IRuntimeAuthorityMediator {

	void setGrant(String grant);
    void setGrants(String[] grants);

    @Deprecated
    void setPermission(UserPermission role);
    @Deprecated
	void setPermissions(UserPermission[] roles);
	
	String[] getGrants();
}