package sk.seges.acris.security.server.acl.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.acls.sid.GrantedAuthoritySid;
import org.springframework.security.acls.sid.PrincipalSid;
import org.springframework.security.acls.sid.Sid;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.acl.dao.IAclSidDao;
import sk.seges.acris.security.server.acl.domain.AclSid;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

@Repository
public class AclSidDao extends AbstractHibernateCRUD<AclSid> implements IAclSidDao {

	public AclSidDao() {
		super(AclSid.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Transactional
    public AclSid loadOrCreate(Sid sid) {

        String sidName = null;
        boolean principal = true;

        if (sid instanceof PrincipalSid) {
            sidName = ((PrincipalSid) sid).getPrincipal();
        } else if (sid instanceof GrantedAuthoritySid) {
            sidName = ((GrantedAuthoritySid) sid).getGrantedAuthority();
            principal = false;
        } else {
            throw new IllegalArgumentException("Unsupported implementation of Sid");
        }

        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(AclSid.A_SID, sidName));
		criteria.add(Restrictions.eq(AclSid.A_PRINCIPAL, principal));

		List<AclSid> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));
        
        if (entries.size() == 0) {
	        AclSid aclSecurityID = new AclSid();
	        aclSecurityID.setPrincipal(principal);
	        aclSecurityID.setSid(sidName);
	        persist(aclSecurityID);
	        return aclSecurityID; 
        } 
        
        if (entries.size() == 1) {
        	return entries.get(0);
        } 
        
        throw new IllegalArgumentException("More than one unique SID found in database");
    } 
}