package sk.seges.acris.security.server.core.acl.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclSecuredObjectIdentity;
import sk.seges.acris.security.server.acl.dao.IAclObjectIdentityDao;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentityMetaModel;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

public class HibernateAclSecuredObjectIdentityDao extends AbstractHibernateCRUD<JpaAclSecuredObjectIdentity> implements
		IAclObjectIdentityDao<JpaAclSecuredObjectIdentity> {

	public HibernateAclSecuredObjectIdentityDao() {
		super(JpaAclSecuredObjectIdentity.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	public JpaAclSecuredObjectIdentity findByObjectId(long objectIdClass, long objectIdIdentity) {

		DetachedCriteria criteria = createCriteria();

		if (objectIdClass != -1) {
			criteria.add(Restrictions.eq(AclSecuredObjectIdentityMetaModel.OBJECT_ID_CLASS.THIS + ".id", objectIdClass));
		}
		criteria.add(Restrictions.eq(AclSecuredObjectIdentityMetaModel.OBJECT_ID_IDENTITY, objectIdIdentity));

		return findUnique(criteria);
	}
	
	private JpaAclSecuredObjectIdentity findUnique(DetachedCriteria detachedCriteria) {
		List<JpaAclSecuredObjectIdentity> entries = findByCriteria(detachedCriteria, new Page(0, Page.ALL_RESULTS));

		if (entries.size() == 0) {
			return null;
		}

		if (entries.size() == 1) {
			return entries.get(0);
		}

		throw new IllegalArgumentException("More than one unique records was found in database");
	}
	
	@Override
	public List<JpaAclSecuredObjectIdentity> findByParent(JpaAclSecuredObjectIdentity parentObjectIdentityId) {
		DetachedCriteria criteria = createCriteria();

		criteria.add(Restrictions.eq(AclSecuredObjectIdentityMetaModel.PARENT_OBJECT, parentObjectIdentityId));

		return findByCriteria(criteria, Page.ALL_RESULTS_PAGE);
	}

	@Override
	public AclSecuredObjectIdentity findById(long id) {
		return entityManager.find(clazz, id);
	}

	@Override
	public AclSecuredObjectIdentity createDefaultEntity() {
		return new JpaAclSecuredObjectIdentity();
	}
}
