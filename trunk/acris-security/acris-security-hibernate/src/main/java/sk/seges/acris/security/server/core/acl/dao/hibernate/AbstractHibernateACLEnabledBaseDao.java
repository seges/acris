package sk.seges.acris.security.server.core.acl.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

public abstract class AbstractHibernateACLEnabledBaseDao<T extends ISecuredObject> extends AbstractHibernateCRUD<T> {

	private AclManager aclManager;

	public AbstractHibernateACLEnabledBaseDao(AclManager aclManager, Class<? extends T> clazz) {
		super(clazz);
		this.aclManager = aclManager;
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	public T add(final T t) {
		T result = super.persist(t);
		aclManager.setAclRecords(t, new Permission[] {Permission.VIEW, Permission.EDIT, Permission.CREATE, Permission.DELETE});
		return result;
	}

	public void remove(T entity) {
		super.remove(entity);
		aclManager.removeSecuredObjectIdentity(entity);
	}
}