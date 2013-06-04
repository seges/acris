package sk.seges.acris.security.server.acl.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.acl.dao.IAclSecuredClassDescriptionDao;
import sk.seges.acris.security.server.acl.domain.AclSecuredClassDescription;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

@Repository
public class AclSecuredClassDescriptionDao extends AbstractHibernateCRUD<AclSecuredClassDescription> implements IAclSecuredClassDescriptionDao {
	public AclSecuredClassDescriptionDao() {
		super(AclSecuredClassDescription.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Transactional
    public AclSecuredClassDescription load(Class<?> clazz) {
        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(AclSecuredClassDescription.CLASS_NAME_FIELD, clazz.getName()));

		List<AclSecuredClassDescription> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));

        if (entries.size() == 0) {
            return null;
        } 
        
        if (entries.size() == 1) {
        	return entries.get(0);
        }
    	
        throw new IllegalArgumentException("More than one unique secured classes found in database");
    } 

    @Transactional
    public AclSecuredClassDescription loadOrCreate(Class<?> clazz) {
        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(AclSecuredClassDescription.CLASS_NAME_FIELD, clazz.getName()));

		List<AclSecuredClassDescription> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));

        if (entries.size() == 0) {
            AclSecuredClassDescription aclSecuredClass = new AclSecuredClassDescription();
        	aclSecuredClass.setClassName(clazz.getName());
        	return persist(aclSecuredClass);
        } 
        
        if (entries.size() == 1) {
        	return entries.get(0);
        }
    	
        throw new IllegalArgumentException("More than one unique secured classes found in database");
    } 
}