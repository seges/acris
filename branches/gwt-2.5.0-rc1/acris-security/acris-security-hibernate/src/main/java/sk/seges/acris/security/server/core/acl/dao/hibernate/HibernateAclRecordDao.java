package sk.seges.acris.security.server.core.acl.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import sk.seges.acris.security.acl.server.model.data.AclEntryData;
import sk.seges.acris.security.acl.server.model.data.AclSecuredClassDescriptionData;
import sk.seges.acris.security.acl.server.model.data.AclSecuredObjectIdentityData;
import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclEntry;
import sk.seges.acris.security.server.core.acl.dao.api.IAclRecordDao;
import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.utils.CastUtils;

public class HibernateAclRecordDao extends AbstractHibernateCRUD<JpaAclEntry> implements IAclRecordDao<JpaAclEntry> {

	public HibernateAclRecordDao() {
		super(JpaAclEntry.class); 
	}

	protected HibernateAclRecordDao(Class<? extends JpaAclEntry> clazz) {
		super(clazz);
	}

	@Override
	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	protected static final String HQL_ACL_DELETE_FROM_TABLE = "delete from " + JpaAclEntry.class.getSimpleName() + " acl where acl_object_identity=:input";
	protected static final String HQL_ACL_SELECT_SID_OBJECT_FROM_TABLE = "from " + JpaAclEntry.class.getSimpleName() + " acl where acl."
			+ AclEntryData.OBJECT_IDENTITY + "." + AclSecuredObjectIdentityData.OBJECT_ID_IDENTITY + "=:objectIdentityId and " + " acl."
			+ AclEntryData.OBJECT_IDENTITY + "." + AclSecuredObjectIdentityData.OBJECT_ID_CLASS + "."
			+ AclSecuredClassDescriptionData.CLASS_NAME + "=:classname and " + " acl." + AclEntryData.SID + "." + AclSidData.SID
			+ "=:sid and " + " acl." + AclEntryData.SID + "." + AclSidData.PRINCIPAL + "=:principal";
	protected static final String HQL_ACL_SELECT_WITHOUT_SID_OBJECT_FROM_TABLE = "from " + JpaAclEntry.class.getSimpleName() + " acl where acl."
			+ AclEntryData.OBJECT_IDENTITY + "." + AclSecuredObjectIdentityData.OBJECT_ID_IDENTITY + "=:objectIdentityId and " + " acl."
			+ AclEntryData.OBJECT_IDENTITY + "." + AclSecuredObjectIdentityData.OBJECT_ID_CLASS + "."
			+ AclSecuredClassDescriptionData.CLASS_NAME + "=:classname";
	protected static final String HQL_ACL_SELECT_SID_OBJECT_BY_CLASSNAME_FROM_TABLE = "from " + JpaAclEntry.class.getSimpleName() + " acl where acl."
			+ AclEntryData.OBJECT_IDENTITY + "." + AclSecuredObjectIdentityData.OBJECT_ID_CLASS + "."
			+ AclSecuredClassDescriptionData.CLASS_NAME + "=:classname and " + " acl." + AclEntryData.SID + "." + AclSidData.SID
			+ "=:sid and " + " acl." + AclEntryData.SID + "." + AclSidData.PRINCIPAL + "=:principal";

	@Override
	public List<AclEntryData> findByIdentityId(long aclObjectIdentity) {
		DetachedCriteria criteria = createCriteria();
		//FIXME .id
		criteria.add(Restrictions.eq(AclEntryData.OBJECT_IDENTITY + ".id", aclObjectIdentity));
		List<JpaAclEntry> result = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));
		return CastUtils.cast(result, AclEntryData.class);
	}
	
	@Override
	public void deleteByIdentityIdAndSid(Long aclId, Class<? extends ISecuredObject<?>> clazz, AclSidData sid) {
		deleteByIdentityIdAndSid(aclId, clazz, sid, clazz.getName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteByIdentityIdAndSid(Long aclId, Class<? extends ISecuredObject<?>> clazz, AclSidData sid, String className) {
		Query query;
		if (sid != null) {
			query = entityManager.createQuery(HQL_ACL_SELECT_SID_OBJECT_FROM_TABLE);
			query.setParameter("sid", sid.getSid());
			query.setParameter("principal", sid.isPrincipal());
		} else {
			query = entityManager.createQuery(HQL_ACL_SELECT_WITHOUT_SID_OBJECT_FROM_TABLE);
		}
		query.setParameter("objectIdentityId", aclId);
		query.setParameter("classname", className);

		//		if (sid instanceof PrincipalSid) {
		//			query.setParameter("sid", ((PrincipalSid)sid).getPrincipal());
		//			query.setParameter("principal", true);
		//		} else if (sid instanceof GrantedAuthoritySid) {
		//			query.setParameter("sid", ((GrantedAuthoritySid)sid).getGrantedAuthority());
		//			query.setParameter("principal", false);
		//		} else {
		//			throw new IllegalArgumentException("Not supported instance of Sid!!");
		//		}

		List<JpaAclEntry> entries = query.getResultList();

		for (JpaAclEntry entry : entries) {
			remove(entry);
		}
	}

	@Override
	public void deleteByClassnameAndSid(Class<? extends ISecuredObject<?>> securedClass, AclSidData sid) {
		List<AclEntryData> entries = findByClassnameAndSid(securedClass, sid);

		for (AclEntryData entry : entries) {
			remove(entry);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AclEntryData> findByClassnameAndSid(Class<? extends ISecuredObject<?>> securedClass, AclSidData sid) {

		Query query = entityManager.createQuery(HQL_ACL_SELECT_SID_OBJECT_BY_CLASSNAME_FROM_TABLE);
		query.setParameter("classname", securedClass.getName());
		query.setParameter("sid", sid.getSid());
		query.setParameter("principal", sid.isPrincipal());

		//        if (sid instanceof PrincipalSid) {
		//            query.setParameter("sid", ((PrincipalSid)sid).getPrincipal());
		//            query.setParameter("principal", true);
		//        } else if (sid instanceof GrantedAuthoritySid) {
		//            query.setParameter("sid", ((GrantedAuthoritySid)sid).getGrantedAuthority());
		//            query.setParameter("principal", false);
		//        } else {
		//            throw new IllegalArgumentException("Not supported instance of Sid!!");
		//        }

		List<AclEntryData> entries = query.getResultList();
		return entries;
	}

	@Override
	public void remove(AclEntryData aclEntry) {
		entityManager.remove(aclEntry);
	}

	@Override
	public void deleteByIdentityId(long aclObjectId) {
		DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(AclEntryData.OBJECT_IDENTITY + ".id", aclObjectId));
		List<JpaAclEntry> entries = findByCriteria(criteria, Page.ALL_RESULTS_PAGE);
		for (JpaAclEntry entry : entries) {
			remove(entry);
		}
	}

	@Override
	public AclEntryData createDefaultEntity() {
		return new JpaAclEntry();
	}
}