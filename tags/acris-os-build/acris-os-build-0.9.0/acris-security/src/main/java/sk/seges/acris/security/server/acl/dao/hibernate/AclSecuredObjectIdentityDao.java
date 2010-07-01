package sk.seges.acris.security.server.acl.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.acl.dao.IAclObjectIdentityDao;
import sk.seges.acris.security.server.acl.domain.AclSecuredObjectIdentity;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

@Component
public class AclSecuredObjectIdentityDao extends AbstractHibernateCRUD<AclSecuredObjectIdentity> implements IAclObjectIdentityDao {

	public AclSecuredObjectIdentityDao() {
		super(AclSecuredObjectIdentity.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Transactional
    public AclSecuredObjectIdentity findByObjectId(long objectIdClass,
            long objectIdIdentity) {
    	
        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(AclSecuredObjectIdentity.A_CLASS_ID, objectIdClass));
		criteria.add(Restrictions.eq(AclSecuredObjectIdentity.A_OBJECT_IDENTITY_ID, objectIdIdentity));

		List<AclSecuredObjectIdentity> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));

        if (entries.size() == 0) {
            return null;
        } 
        
        if (entries.size() == 1) {
        	return entries.get(0);
        }
    	
        throw new IllegalArgumentException("More than one unique records was found in database");
    }
}
