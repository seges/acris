package sk.seges.acris.security.server.core.acl.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclSecuredClassDescription;
import sk.seges.acris.security.server.acl.dao.IAclSecuredClassDescriptionDao;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredClassDescription;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredClassDescriptionBeanWrapper;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

public class HibernateAclSecuredClassDescriptionDao extends AbstractHibernateCRUD<JpaAclSecuredClassDescription> implements IAclSecuredClassDescriptionDao<JpaAclSecuredClassDescription> {
	public HibernateAclSecuredClassDescriptionDao() {
		super(JpaAclSecuredClassDescription.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

    public AclSecuredClassDescription load(Class<?> clazz) {
        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(AclSecuredClassDescriptionBeanWrapper.CLASS_NAME, clazz.getName()));

		List<JpaAclSecuredClassDescription> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));

        if (entries.size() == 0) {
            return null;
        } 
        
        if (entries.size() == 1) {
        	return entries.get(0);
        }
    	
        throw new IllegalArgumentException("More than one unique secured classes found in database");
    } 

    public AclSecuredClassDescription loadOrCreate(Class<?> clazz) {
        DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(AclSecuredClassDescriptionBeanWrapper.CLASS_NAME, clazz.getName()));

		List<JpaAclSecuredClassDescription> entries = findByCriteria(criteria, new Page(0, Page.ALL_RESULTS));

        if (entries.size() == 0) {
            JpaAclSecuredClassDescription aclSecuredClass = new JpaAclSecuredClassDescription();
        	aclSecuredClass.setClassName(clazz.getName());
        	return persist(aclSecuredClass);
        } 
        
        if (entries.size() == 1) {
        	return entries.get(0);
        }
    	
        throw new IllegalArgumentException("More than one unique secured classes found in database");
    } 
}