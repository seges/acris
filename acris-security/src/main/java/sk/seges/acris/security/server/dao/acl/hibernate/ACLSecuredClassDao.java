package sk.seges.acris.security.server.dao.acl.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.dao.acl.IACLSecuredClassDAO;
import sk.seges.acris.security.server.domain.acl.ACLSecuredClass;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

@Repository
public class ACLSecuredClassDao extends AbstractHibernateCRUD<ACLSecuredClass> implements IACLSecuredClassDAO {
	public ACLSecuredClassDao() {
		super(ACLSecuredClass.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Transactional
    public ACLSecuredClass load(Class<?> clazz) {
        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(ACLSecuredClass.CLASS_NAME_FIELD, clazz.getName()));

		List<ACLSecuredClass> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));

        if (entries.size() == 0) {
            return null;
        } 
        
        if (entries.size() == 1) {
        	return entries.get(0);
        }
    	
        throw new IllegalArgumentException("More than one unique secured classes found in database");
    } 

    @Transactional
    public ACLSecuredClass loadOrCreate(Class<?> clazz) {
        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(ACLSecuredClass.CLASS_NAME_FIELD, clazz.getName()));

		List<ACLSecuredClass> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));

        if (entries.size() == 0) {
            ACLSecuredClass aclSecuredClass = new ACLSecuredClass();
        	aclSecuredClass.setClassName(clazz.getName());
        	return persist(aclSecuredClass);
        } 
        
        if (entries.size() == 1) {
        	return entries.get(0);
        }
    	
        throw new IllegalArgumentException("More than one unique secured classes found in database");
    } 
}