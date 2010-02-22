package sk.seges.acris.security.server.dao.acl.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.acls.sid.GrantedAuthoritySid;
import org.springframework.security.acls.sid.PrincipalSid;
import org.springframework.security.acls.sid.Sid;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.domain.ISecuredObject;
import sk.seges.acris.security.server.dao.acl.IACLEntryDAO;
import sk.seges.acris.security.server.domain.acl.ACLEntry;
import sk.seges.acris.security.server.domain.acl.ACLObjectIdentity;
import sk.seges.acris.security.server.domain.acl.ACLSecuredClass;
import sk.seges.acris.security.server.domain.acl.ACLSecurityID;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

@Repository
public class ACLEntryDao extends AbstractHibernateCRUD<ACLEntry> implements IACLEntryDAO {

	public ACLEntryDao() {
		super(ACLEntry.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	protected static final String HQL_ACL_DELETE_FROM_TABLE = "delete from " + ACLEntry.class.getSimpleName() + " acl where acl.objectIdentity.id=:input";
	protected static final String HQL_ACL_SELECT_SID_OBJECT_FROM_TABLE = "from " + ACLEntry.class.getSimpleName() + 
		" acl where acl." + ACLEntry.OBJECT_IDENTITY_FIELD + "." + ACLObjectIdentity.OBJECT_IDENTITY_ID_FIELD + "=:objectIdentityId and " +
		" acl." + ACLEntry.OBJECT_IDENTITY_FIELD + "." + ACLObjectIdentity.OBJECT_CLASS_FIELD + "." + ACLSecuredClass.CLASS_NAME_FIELD + "=:classname and " +
		" acl." + ACLEntry.SID_FIELD + "." + ACLSecurityID.SID_FIELD + "=:sid and " + 
		" acl." + ACLEntry.SID_FIELD + "." + ACLSecurityID.PRINCIPAL_FIELD + "=:principal";

	@Transactional
    public List<ACLEntry> findByIdentityId(long aclObjectIdentity) {
        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(ACLEntry.OBJECT_IDENTITY_ID_FIELD, aclObjectIdentity));

        return findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));
    }

	@SuppressWarnings("unchecked")
	@Transactional
    public void deleteByIdentityIdAndSid(ISecuredObject securedObject, Sid sid) {

		Query query = entityManager.createQuery(HQL_ACL_SELECT_SID_OBJECT_FROM_TABLE);
		query.setParameter("objectIdentityId", securedObject.getId());
		query.setParameter("classname", securedObject.getClass().getName());
		
		if (sid instanceof PrincipalSid) {
			query.setParameter("sid", ((PrincipalSid)sid).getPrincipal());
			query.setParameter("principal", true);
		} else if (sid instanceof GrantedAuthoritySid) {
			query.setParameter("sid", ((GrantedAuthoritySid)sid).getGrantedAuthority());
			query.setParameter("principal", false);
		} else {
			throw new IllegalArgumentException("Not supported instance of Sid!!");
		}
		
		List<ACLEntry> entries = (List<ACLEntry>)query.getResultList();

		for (ACLEntry entry : entries) {
			remove(entry);
		}
    }

    @Transactional
	public void deleteByClassnameAndSid(Class<? extends ISecuredObject> securedClass, Sid sid) {
        List<ACLEntry> entries = findByClassnameAndSid(securedClass, sid);
        
        for (ACLEntry entry : entries) {
            remove(entry);
        }	    
	}

    @Transactional
    public List<ACLEntry> findByClassnameAndSid(Class<? extends ISecuredObject> securedClass, Sid sid) {

        Query query = entityManager.createQuery(HQL_ACL_SELECT_SID_OBJECT_FROM_TABLE);
        query.setParameter("classname", securedClass.getName());
        
        if (sid instanceof PrincipalSid) {
            query.setParameter("sid", ((PrincipalSid)sid).getPrincipal());
            query.setParameter("principal", true);
        } else if (sid instanceof GrantedAuthoritySid) {
            query.setParameter("sid", ((GrantedAuthoritySid)sid).getGrantedAuthority());
            query.setParameter("principal", false);
        } else {
            throw new IllegalArgumentException("Not supported instance of Sid!!");
        }
        
        List<ACLEntry> entries = (List<ACLEntry>)query.getResultList();
        return entries;
    }
    
	@Transactional
	public void remove(ACLEntry aclEntry) {
		entityManager.remove(aclEntry);
	}

	@Transactional
    public void deleteByIdentityId(long aclObjectId) {
		Query query = entityManager.createQuery(HQL_ACL_DELETE_FROM_TABLE);
		query.setParameter("input", aclObjectId);
		query.executeUpdate();
    }
}