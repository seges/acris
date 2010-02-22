package sk.seges.acris.security.server.dao.acl.hibernate;

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

import sk.seges.acris.security.server.dao.acl.IACLSecurityIDDAO;
import sk.seges.acris.security.server.domain.acl.ACLSecurityID;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

@Repository
public class ACLSecurityIDDao extends AbstractHibernateCRUD<ACLSecurityID> implements IACLSecurityIDDAO {

	public ACLSecurityIDDao() {
		super(ACLSecurityID.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Transactional
    public ACLSecurityID loadOrCreate(Sid sid) {

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
		criteria.add(Restrictions.eq(ACLSecurityID.SID_FIELD, sidName));
		criteria.add(Restrictions.eq(ACLSecurityID.PRINCIPAL_FIELD, principal));

		List<ACLSecurityID> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));
        
        if (entries.size() == 0) {
	        ACLSecurityID aclSecurityID = new ACLSecurityID();
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