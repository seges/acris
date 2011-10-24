package sk.seges.acris.security.server.acl.service.api;

import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public interface AclManager {

    void removeAclRecords(Class<? extends ISecuredObject> securedClass, UserData user);
    
    void removeAclRecords(ISecuredObject securedObject, UserData user);
    
	void removeAclRecords(ISecuredObject securedObject);

	void removeSecuredObjectIdentity(ISecuredObject securedObject);

	public void setAclRecords(ISecuredObject securedObject, Permission[] authorities);

	void setAclRecords(ISecuredObject securedObject, UserData user, Permission[] authorities);

}