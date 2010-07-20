/**
 * 
 */
package sk.seges.corpis.dao;

import javax.persistence.EntityManager;

/**
 * @author eldzi
 *
 */
public abstract class AbstractJPADAO implements IJPAAware {
	protected EntityManager entityManager;

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}	
}
