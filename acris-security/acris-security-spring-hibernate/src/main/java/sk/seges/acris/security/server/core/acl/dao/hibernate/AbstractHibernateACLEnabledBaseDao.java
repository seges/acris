package sk.seges.acris.security.server.core.acl.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.sesam.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

public abstract class AbstractHibernateACLEnabledBaseDao<T extends ISecuredObject<?>> extends AbstractHibernateCRUD<T> {

	protected AclManager aclManager;

	public AbstractHibernateACLEnabledBaseDao(AclManager aclManager, Class<? extends T> clazz) {
		super(clazz);
		this.aclManager = aclManager;
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	public T add(final T entity) {
		T result = super.persist(entity);
		aclManager.setAclRecords(entity, new Permission[] {Permission.VIEW, Permission.EDIT, Permission.CREATE, Permission.DELETE});
		return result;
	}
	
	public void remove(T entity) {
		super.remove(entity);
		aclManager.removeSecuredObjectIdentity(entity.getIdForACL(), entity.getSecuredClass().getName());
	}
	
	public T update(T entity) {
		T result = super.merge(entity);
		aclManager.setAclRecords(entity, new Permission[] {Permission.VIEW, Permission.EDIT, Permission.CREATE, Permission.DELETE});
		return result;
	};
	
	
}