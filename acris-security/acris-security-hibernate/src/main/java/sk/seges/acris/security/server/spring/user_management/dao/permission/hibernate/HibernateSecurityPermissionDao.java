package sk.seges.acris.security.server.spring.user_management.dao.permission.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.seges.acris.security.server.core.user_management.dao.permission.hibernate.IHibernateSecurityPermissionDao;
import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaSecurityPermission;
import sk.seges.acris.security.user_management.server.model.data.HierarchyPermissionData;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

public class HibernateSecurityPermissionDao extends AbstractHibernateCRUD<HierarchyPermissionData> implements IHibernateSecurityPermissionDao<HierarchyPermissionData> {

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager em) {
		super.setEntityManager(em);
	}

	public HibernateSecurityPermissionDao() {
		super(JpaSecurityPermission.class);
	}

	protected HibernateSecurityPermissionDao(Class<? extends HierarchyPermissionData> clazz) {
		super(clazz);
	}
}