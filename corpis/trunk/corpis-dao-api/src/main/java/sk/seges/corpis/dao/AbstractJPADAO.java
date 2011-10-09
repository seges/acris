/**
 * 
 */
package sk.seges.corpis.dao;

import javax.persistence.EntityManager;

import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.domain.IDomainObject;

/**
 * @author eldzi
 *
 */
public abstract class AbstractJPADAO<T extends IDomainObject<?>> implements IJPAAware, ICrudDAO<T> {

	protected EntityManager entityManager;

	protected Class<? extends T> clazz;

    protected AbstractJPADAO(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}	
	
    @Override
    public T findEntity(T entity) {
        return entityManager.find(clazz, entity.getId());
    }

    @Override
    public T merge(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public T persist(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void remove(T entity) {
        entityManager.remove(entity);
    }
}