package sk.seges.acris.security.server.spring.user_management.dao.permission.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.seges.acris.security.server.core.user_management.dao.permission.hibernate.IHibernateSecurityPermissionDao;
import sk.seges.acris.security.shared.core.user_management.domain.jpa.JpaSecurityPermission;
import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermission;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

public class HibernateSecurityPermissionDao extends AbstractHibernateCRUD<HierarchyPermission> implements IHibernateSecurityPermissionDao<HierarchyPermission> {

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager em) {
		super.setEntityManager(em);
	}

	public HibernateSecurityPermissionDao() {
		super(JpaSecurityPermission.class);
	}

	protected HibernateSecurityPermissionDao(Class<? extends HierarchyPermission> clazz) {
		super(clazz);
	}
}