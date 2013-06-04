package sk.seges.acris.security.server.acl.dao.hibernate;

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
import sk.seges.acris.security.server.acl.dao.IAclRecordDao;
import sk.seges.acris.security.server.acl.domain.AclSid;
import sk.seges.acris.security.server.acl.domain.AclEntry;
import sk.seges.acris.security.server.acl.domain.AclSecuredClassDescription;
import sk.seges.acris.security.server.acl.domain.AclSecuredObjectIdentity;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

@Repository
public class AclRecordDao extends AbstractHibernateCRUD<AclEntry> implements IAclRecordDao {

	public AclRecordDao() {
		super(AclEntry.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	protected static final String HQL_ACL_DELETE_FROM_TABLE = "delete from " + AclEntry.class.getSimpleName() + " acl where acl.objectIdentity.id=:input";
	protected static final String HQL_ACL_SELECT_SID_OBJECT_FROM_TABLE = "from " + AclEntry.class.getSimpleName() + 
		" acl where acl." + AclEntry.A_OBJECT_IDENTITY + "." + AclSecuredObjectIdentity.A_OBJECT_IDENTITY_ID + "=:objectIdentityId and " +
		" acl." + AclEntry.A_OBJECT_IDENTITY + "." + AclSecuredObjectIdentity.A_OBJECT_CLASS + "." + AclSecuredClassDescription.CLASS_NAME_FIELD + "=:classname and " +
		" acl." + AclEntry.A_SID + "." + AclSid.A_SID + "=:sid and " + 
		" acl." + AclEntry.A_SID + "." + AclSid.A_PRINCIPAL + "=:principal";
    protected static final String HQL_ACL_SELECT_SID_OBJECT_BY_CLASSNAME_FROM_TABLE = "from " + AclEntry.class.getSimpleName() + 
        " acl where acl." + AclEntry.A_OBJECT_IDENTITY + "." + AclSecuredObjectIdentity.A_OBJECT_CLASS + "." + AclSecuredClassDescription.CLASS_NAME_FIELD + "=:classname and " +
        " acl." + AclEntry.A_SID + "." + AclSid.A_SID + "=:sid and " + 
        " acl." + AclEntry.A_SID + "." + AclSid.A_PRINCIPAL + "=:principal";

	@Transactional
    public List<AclEntry> findByIdentityId(long aclObjectIdentity) {
        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(AclEntry.A_OBJECT_IDENTITY_ID, aclObjectIdentity));

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
		
		List<AclEntry> entries = (List<AclEntry>)query.getResultList();

		for (AclEntry entry : entries) {
			remove(entry);
		}
    }

    @Transactional
	public void deleteByClassnameAndSid(Class<? extends ISecuredObject> securedClass, Sid sid) {
        List<AclEntry> entries = findByClassnameAndSid(securedClass, sid);
        
        for (AclEntry entry : entries) {
            remove(entry);
        }	    
	}

    @Transactional
    public List<AclEntry> findByClassnameAndSid(Class<? extends ISecuredObject> securedClass, Sid sid) {

        Query query = entityManager.createQuery(HQL_ACL_SELECT_SID_OBJECT_BY_CLASSNAME_FROM_TABLE);
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
        
        List<AclEntry> entries = (List<AclEntry>)query.getResultList();
        return entries;
    }
    
	@Transactional
	public void remove(AclEntry aclEntry) {
		entityManager.remove(aclEntry);
	}

	@Transactional
    public void deleteByIdentityId(long aclObjectId) {
		Query query = entityManager.createQuery(HQL_ACL_DELETE_FROM_TABLE);
		query.setParameter("input", aclObjectId);
		query.executeUpdate();
    }
}