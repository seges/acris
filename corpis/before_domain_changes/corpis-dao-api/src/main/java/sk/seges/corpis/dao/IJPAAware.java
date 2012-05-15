/**
 * 
 */
package sk.seges.corpis.dao;

import javax.persistence.EntityManager;

/**
 * @author eldzi
 */
public interface IJPAAware {
	void setEntityManager(EntityManager entityManager);
}
