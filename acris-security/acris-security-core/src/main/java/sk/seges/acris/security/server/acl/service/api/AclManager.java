package sk.seges.acris.security.server.acl.service.api;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.security.shared.domain.ISecuredObject;

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
	void setAclRecords(ISecuredObject<?> securedObject, String authorityName, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void setAclRecords(ISecuredObject<?> securedObject, String authorityName, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions, boolean updateParent);
	
	
	void setAclRecords(Class<? extends ISecuredObject<?>> objectClass, Long aclId, UserData user,	Permission[] authorities);
	
	void resetAclRecords(Class<? extends ISecuredObject<?>> objectClass, Long aclId, UserData user, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void resetAclRecords(Class<? extends ISecuredObject<?>> objectClass, Long aclId, RoleData role, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	void resetAclRecords(Class<? extends ISecuredObject<?>> objectClass, Long aclId, String userName, sk.seges.acris.security.shared.user_management.domain.Permission[] permissions);
	
	List<String> loadSidNames(ISecuredObject<?> securedObject);
	List<String> loadSidNames(Class<? extends ISecuredObject<?>> clazz, Long securedId);
}