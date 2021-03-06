package sk.seges.acris.security.server.spring.acl.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.acl.server.model.data.AclSecuredClassDescriptionData;
import sk.seges.acris.security.server.core.acl.dao.hibernate.HibernateAclSecuredClassDescriptionDao;

@Repository
public class HibernateSpringSecuredClassDescriptionDao extends HibernateAclSecuredClassDescriptionDao {

	@Transactional
	public AclSecuredClassDescriptionData load(Class<?> clazz) {
		return super.load(clazz);
	}

	@Transactional
	public AclSecuredClassDescriptionData loadOrCreate(Class<?> clazz) {
		return super.loadOrCreate(clazz);
	}
}