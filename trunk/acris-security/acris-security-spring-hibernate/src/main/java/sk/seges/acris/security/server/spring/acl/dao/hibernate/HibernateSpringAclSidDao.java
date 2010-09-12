package sk.seges.acris.security.server.spring.acl.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.core.acl.dao.hibernate.HibernateAclSidDao;
import sk.seges.acris.security.server.core.acl.domain.api.AclSid;

@Repository
public class HibernateSpringAclSidDao extends HibernateAclSidDao {

	@Transactional
    public AclSid loadOrCreate(String sidName, boolean principal) {
		return super.loadOrCreate(sidName, principal);
    } 
}