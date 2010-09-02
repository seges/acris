package sk.seges.acris.security.server.user_management.dao.permission.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaSecurityPermission;
import sk.seges.acris.security.server.user_management.dao.permission.ISecurityPermissionDao;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

public class HibernateSecurityPermissionDao extends AbstractHibernateCRUD<JpaSecurityPermission> implements ISecurityPermissionDao<JpaSecurityPermission> {

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }

    public HibernateSecurityPermissionDao() {
        super(JpaSecurityPermission.class);
    }
}
