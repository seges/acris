/**
 * 
 */
package sk.seges.corpis.dataset;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Helper class used to ease manipulation with test data sets and their relation to DB.
 * 
 * @author eldzi
 */
@Component
public class TestDataSetHelper {
	public static final String TEST_ENTITY_MANAGER_FACTORY = "corpisTestEntityManagerFactory";
	
	@PersistenceContext(unitName = TEST_ENTITY_MANAGER_FACTORY)
	protected EntityManager entityManager;
	
	@Transactional
	public void deleteAllInEntity(String entityTableName) {
		entityManager.createNativeQuery("delete from " + entityTableName).executeUpdate();
	}
	
	@Transactional
	public void deleteAllInEntityHQL(String entityTableName) {
		entityManager.createQuery("delete from " + entityTableName).executeUpdate();
	}
}
