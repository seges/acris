package sk.seges.acris.security.server.spring.acl.dao.hibernate;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.server.core.acl.dao.hibernate.AbstractHibernateACLEnabledBaseDao;
import sk.seges.acris.security.shared.domain.ISecuredObject;

public abstract class AbstractHibernateSpringACLEnabledBaseDao<T extends ISecuredObject<T>> extends AbstractHibernateACLEnabledBaseDao<T> {

	public AbstractHibernateSpringACLEnabledBaseDao(AclManager aclManager, Class<T> clazz) {
		super(aclManager, clazz);
	}

	@Transactional
	public T add(final T t) {
		return super.add(t);
	}

	@Transactional
	public void remove(T entity) {
		super.remove(entity);
	}
}