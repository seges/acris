package sk.seges.acris.security.server.spring.acl.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.core.acl.dao.hibernate.HibernateAclSecuredClassDescriptionDao;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredClassDescription;

@Repository
public class HibernateSpringSecuredClassDescriptionDao extends HibernateAclSecuredClassDescriptionDao {

	@Transactional
	public AclSecuredClassDescription load(Class<?> clazz) {
		return super.load(clazz);
	}

	@Transactional
	public AclSecuredClassDescription loadOrCreate(Class<?> clazz) {
		return super.loadOrCreate(clazz);
	}
}