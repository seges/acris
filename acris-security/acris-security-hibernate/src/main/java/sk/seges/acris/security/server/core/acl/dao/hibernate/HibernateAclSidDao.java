package sk.seges.acris.security.server.core.acl.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclSid;
import sk.seges.acris.security.server.core.acl.dao.api.IAclSidDao;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

public class HibernateAclSidDao extends AbstractHibernateCRUD<JpaAclSid> implements IAclSidDao<JpaAclSid> {

	public HibernateAclSidDao() {
		super(JpaAclSid.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

    public AclSidData loadOrCreate(String sidName, boolean principal) {

        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(AclSidData.SID, sidName));
		criteria.add(Restrictions.eq(AclSidData.PRINCIPAL, principal));

		List<JpaAclSid> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));
        
        if (entries.size() == 0) {
	        JpaAclSid aclSecurityID = new JpaAclSid();
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