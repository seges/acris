package sk.seges.acris.security.server.dao.acl.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.dao.acl.IACLObjectIdentityDAO;
import sk.seges.acris.security.server.domain.acl.ACLObjectIdentity;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

@Component
public class ACLObjectIdentityDao extends AbstractHibernateCRUD<ACLObjectIdentity> implements IACLObjectIdentityDAO {

	public ACLObjectIdentityDao() {
		super(ACLObjectIdentity.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Transactional
    public ACLObjectIdentity findByObjectId(long objectIdClass,
            long objectIdIdentity) {
    	
        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(ACLObjectIdentity.CLASS_ID_FIELD, objectIdClass));
		criteria.add(Restrictions.eq(ACLObjectIdentity.OBJECT_IDENTITY_ID_FIELD, objectIdIdentity));

		List<ACLObjectIdentity> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));

        if (entries.size() == 0) {
            return null;
        } 
        
        if (entries.size() == 1) {
        	return entries.get(0);
        }
    	
        throw new IllegalArgumentException("More than one unique records was found in database");
    }
}
