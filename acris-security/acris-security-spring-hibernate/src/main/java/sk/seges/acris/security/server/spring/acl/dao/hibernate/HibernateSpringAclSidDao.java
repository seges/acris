package sk.seges.acris.security.server.spring.acl.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.acris.security.server.core.acl.dao.hibernate.HibernateAclSidDao;

@Repository
public class HibernateSpringAclSidDao extends HibernateAclSidDao {

	@Transactional
    public AclSidData loadOrCreate(String sidName, boolean principal) {
		return super.loadOrCreate(sidName, principal);
    } 
}