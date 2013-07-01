package sk.seges.acris.security.server.core.acl.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.security.server.model.acl.AclSecuredEntity;

public abstract class AbstractHibernateACLEnabledBaseDao<T extends IDomainObject<?>> extends AbstractHibernateCRUD<T> {

	protected AclManager aclManager;

	public AbstractHibernateACLEnabledBaseDao(AclManager aclManager, Class<? extends T> clazz) {
		super(clazz);
		this.aclManager = aclManager;
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	public T add(final AclSecuredEntity<IDomainObject<?>> entity) {
		@SuppressWarnings("unchecked")
		T result = super.persist((T) entity.getEntity());
		aclManager.setAclRecords(entity.getAclData(), new Permission[] {Permission.VIEW, Permission.EDIT, Permission.CREATE, Permission.DELETE});
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public void remove(AclSecuredEntity<IDomainObject<?>> entity) {
		super.remove((T) entity.getEntity());
		aclManager.removeSecuredObjectIdentity(entity.getAclData().getAclId(), entity.getAclData().getClassName());
	}
	
	public T update(AclSecuredEntity<IDomainObject<?>> entity) {
		@SuppressWarnings("unchecked")
		T result = super.merge((T) entity.getEntity());
		aclManager.setAclRecords(entity.getAclData(), new Permission[] {Permission.VIEW, Permission.EDIT, Permission.CREATE, Permission.DELETE});
		return result;
	};
	
	
}