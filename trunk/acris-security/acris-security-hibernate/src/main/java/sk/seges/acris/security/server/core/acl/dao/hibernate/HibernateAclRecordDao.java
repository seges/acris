package sk.seges.acris.security.server.core.acl.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclEntry;
import sk.seges.acris.security.server.acl.dao.IAclRecordDao;
import sk.seges.acris.security.server.core.acl.domain.api.AclEntry;
import sk.seges.acris.security.server.core.acl.domain.api.AclEntryMetaModel;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredClassDescriptionMetaModel;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentityMetaModel;
import sk.seges.acris.security.server.core.acl.domain.api.AclSid;
import sk.seges.acris.security.server.core.acl.domain.api.AclSidMetaModel;
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

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	protected static final String HQL_ACL_DELETE_FROM_TABLE = "delete from " + JpaAclEntry.class.getSimpleName() + " acl where acl.objectIdentity.id=:input";
	protected static final String HQL_ACL_SELECT_SID_OBJECT_FROM_TABLE = "from " + JpaAclEntry.class.getSimpleName() + " acl where acl."
			+ AclEntryMetaModel.OBJECT_IDENTITY.THIS + "." + AclSecuredObjectIdentityMetaModel.OBJECT_ID_IDENTITY + "=:objectIdentityId and " + " acl."
			+ AclEntryMetaModel.OBJECT_IDENTITY.THIS + "." + AclSecuredObjectIdentityMetaModel.OBJECT_ID_CLASS.THIS + "."
			+ AclSecuredClassDescriptionMetaModel.CLASS_NAME + "=:classname and " + " acl." + AclEntryMetaModel.SID.THIS + "." + AclSidMetaModel.SID
			+ "=:sid and " + " acl." + AclEntryMetaModel.SID.THIS + "." + AclSidMetaModel.PRINCIPAL + "=:principal";
	protected static final String HQL_ACL_SELECT_SID_OBJECT_BY_CLASSNAME_FROM_TABLE = "from " + JpaAclEntry.class.getSimpleName() + " acl where acl."
			+ AclEntryMetaModel.OBJECT_IDENTITY.THIS + "." + AclSecuredObjectIdentityMetaModel.OBJECT_ID_CLASS.THIS + "."
			+ AclSecuredClassDescriptionMetaModel.CLASS_NAME + "=:classname and " + " acl." + AclEntryMetaModel.SID.THIS + "." + AclSidMetaModel.SID
			+ "=:sid and " + " acl." + AclEntryMetaModel.SID.THIS + "." + AclSidMetaModel.PRINCIPAL + "=:principal";

	@Override
	public List<AclEntry> findByIdentityId(long aclObjectIdentity) {
		DetachedCriteria criteria = createCriteria();
		//FIXME .id
		criteria.add(Restrictions.eq(AclEntryMetaModel.OBJECT_IDENTITY.THIS + ".id", aclObjectIdentity));
		List<JpaAclEntry> result = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));
		return CastUtils.cast(result, AclEntry.class);
	}

	@Override
	public void deleteByIdentityIdAndSid(Long aclId, Class clazz, AclSid sid) {
		deleteByIdentityIdAndSid(aclId, clazz, sid, clazz.getName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteByIdentityIdAndSid(Long aclId, Class clazz, AclSid sid, String className) {
		Query query = entityManager.createQuery(HQL_ACL_SELECT_SID_OBJECT_FROM_TABLE);
		query.setParameter("objectIdentityId", aclId);
		query.setParameter("classname", className);
		query.setParameter("sid", sid.getSid());
		query.setParameter("principal", sid.isPrincipal());

		//		if (sid instanceof PrincipalSid) {
		//			query.setParameter("sid", ((PrincipalSid)sid).getPrincipal());
		//			query.setParameter("principal", true);
		//		} else if (sid instanceof GrantedAuthoritySid) {
		//			query.setParameter("sid", ((GrantedAuthoritySid)sid).getGrantedAuthority());
		//			query.setParameter("principal", false);
		//		} else {
		//			throw new IllegalArgumentException("Not supported instance of Sid!!");
		//		}

		List<JpaAclEntry> entries = (List<JpaAclEntry>) query.getResultList();

		for (JpaAclEntry entry : entries) {
			remove(entry);
		}
	}

	public void deleteByClassnameAndSid(Class<? extends ISecuredObject> securedClass, AclSid sid) {
		List<AclEntry> entries = findByClassnameAndSid(securedClass, sid);

		for (AclEntry entry : entries) {
			remove(entry);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AclEntry> findByClassnameAndSid(Class<? extends ISecuredObject> securedClass, AclSid sid) {

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

		List<AclEntry> entries = (List<AclEntry>) query.getResultList();
		return entries;
	}

	@Override
	public void remove(AclEntry aclEntry) {
		entityManager.remove(aclEntry);
	}

	@Override
	public void deleteByIdentityId(long aclObjectId) {
		Query query = entityManager.createQuery(HQL_ACL_DELETE_FROM_TABLE);
		query.setParameter("input", aclObjectId);
		query.executeUpdate();
	}

	@Override
	public AclEntry createDefaultEntity() {
		return new JpaAclEntry();
	}
}