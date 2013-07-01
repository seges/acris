package sk.seges.acris.security.server.spring.acl.dao.hibernate;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.server.core.acl.dao.hibernate.AbstractHibernateACLEnabledBaseDao;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.security.server.model.acl.AclSecuredEntity;

public abstract class AbstractHibernateSpringACLEnabledBaseDao<T extends IDomainObject<?>> extends AbstractHibernateACLEnabledBaseDao<T> {

	public AbstractHibernateSpringACLEnabledBaseDao(AclManager aclManager, Class<T> clazz) {
		super(aclManager, clazz);
	}

	@Transactional
	public T add(final AclSecuredEntity<IDomainObject<?>> t) {
		return super.add(t);
	}

	@Transactional
	public void remove(AclSecuredEntity<IDomainObject<?>> entity) {
		super.remove(entity);
	}
}