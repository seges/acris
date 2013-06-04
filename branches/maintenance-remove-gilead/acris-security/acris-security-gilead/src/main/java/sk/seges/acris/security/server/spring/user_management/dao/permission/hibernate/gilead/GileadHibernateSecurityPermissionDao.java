package sk.seges.acris.security.server.spring.user_management.dao.permission.hibernate.gilead;

import sk.seges.acris.security.server.core.user_management.dao.permission.ISecurityPermissionDao;
import sk.seges.acris.security.server.spring.user_management.dao.permission.hibernate.HibernateSecurityPermissionDao;
import sk.seges.acris.security.shared.core.user_management.domain.jpa.gilead.GileadJpaSecurityPermission;
import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermission;

//@Repository
//@Qualifier(value = "securityPermissionDao")
public class GileadHibernateSecurityPermissionDao extends HibernateSecurityPermissionDao implements ISecurityPermissionDao<HierarchyPermission> {

    public GileadHibernateSecurityPermissionDao() {
        super(GileadJpaSecurityPermission.class);
    }
}