package sk.seges.acris.security.server.acl.service.api;

import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.user_management.domain.api.RoleData;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public interface AclManager {

    void removeAclRecords(Class<? extends ISecuredObject<?>> securedClass, UserData user);
    void removeAclRecords(Long aclId, String className, UserData user);
	void removeAclRecords(Long aclId, String className);

	void removeSecuredObjectIdentity(Long aclId, String className);
	
	void removeAcl(ISecuredObject<?> securedObject);

	void setAclRecords(ISecuredObject<?> securedObject, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void setAclRecords(ISecuredObject<?> securedObject, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent);
	void setAclRecords(ISecuredObject<?> securedObject, UserData user, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void setAclRecords(ISecuredObject<?> securedObject, UserData user, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent);
	void setAclRecords(ISecuredObject<?> securedObject, RoleData role, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void setAclRecords(ISecuredObject<?> securedObject, RoleData role, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent);
	
	void resetAclRecords(Class<? extends ISecuredObject<?>> objectClass, Long aclId, UserData user, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void resetAclRecords(Class<? extends ISecuredObject<?>> objectClass, Long aclId, RoleData role, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);

}