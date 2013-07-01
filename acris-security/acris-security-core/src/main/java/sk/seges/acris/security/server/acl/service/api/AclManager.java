package sk.seges.acris.security.server.acl.service.api;

import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.security.server.model.acl.AclSecurityData;
import sk.seges.sesam.security.shared.model.api.PermissionData;

public interface AclManager {

	PermissionData getPermissionData(AclSecurityData aclSecurityData);

    void removeAclRecords(Class<? extends IDomainObject<?>> securedClass, UserData user);
    void removeAclRecords(Long aclId, String className, UserData user);
	void removeAclRecords(Long aclId, String className);

	void removeSecuredObjectIdentity(Long aclId, String className);
	
	void removeAcl(AclSecurityData securedObject);

	void setAclRecords(AclSecurityData securedObject, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void setAclRecords(AclSecurityData securedObject, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent);
	void setAclRecords(AclSecurityData securedObject, UserData user, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void setAclRecords(AclSecurityData securedObject, UserData user, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent);
	void setAclRecords(AclSecurityData securedObject, RoleData role, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void setAclRecords(AclSecurityData securedObject, RoleData role, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent);
	
	void setAclRecords(Class<? extends IDomainObject<?>> objectClass, Long aclId, UserData user, Permission[] authorities);
	
	void resetAclRecords(Class<? extends IDomainObject<?>> objectClass, Long aclId, UserData user, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void resetAclRecords(Class<? extends IDomainObject<?>> objectClass, Long aclId, RoleData role, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void resetAclRecords(Class<? extends IDomainObject<?>> objectClass, Long aclId, String userName, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);

}