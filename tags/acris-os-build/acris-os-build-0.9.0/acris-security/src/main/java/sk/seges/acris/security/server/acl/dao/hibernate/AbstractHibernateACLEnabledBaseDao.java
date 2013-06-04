package sk.seges.acris.security.server.acl.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.domain.ISecuredObject;
import sk.seges.acris.security.rpc.user_management.domain.Permission;
import sk.seges.acris.security.server.acl.service.AclMaintainer;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

public abstract class AbstractHibernateACLEnabledBaseDao<T extends ISecuredObject> extends AbstractHibernateCRUD<T> {

	@Autowired
	private AclMaintainer aclManager;
	
	public AbstractHibernateACLEnabledBaseDao(Class<T> clazz) {
		super(clazz);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Transactional
	public T add(final T t) {
		T result = super.persist(t);
		aclManager.setAclRecords(t, new Permission[] {
				Permission.VIEW, 
				Permission.EDIT, 
				Permission.CREATE, 
				Permission.DELETE});
		return result;
	}

	@Transactional
	public void remove(T entity) {
		super.remove(entity);
		aclManager.removeSecuredObjectIdentity(entity);
	}
}