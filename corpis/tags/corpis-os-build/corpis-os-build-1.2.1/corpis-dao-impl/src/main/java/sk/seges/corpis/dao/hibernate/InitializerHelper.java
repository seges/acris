/**
 * 
 */
package sk.seges.corpis.dao.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.impl.SessionImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * Helper class for initializer where Spring is able to wrap it to proxy. Using
 * init-method Spring was not able to wrap method so this delegation was
 * introduced.
 * 
 * @author eldzi
 */
public class InitializerHelper {
	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Transactional
	public void createSequence(String sequenceName, Integer initialValue,
			Integer incrementSize) {
		SessionImpl imp = (SessionImpl) entityManager.getDelegate();
		String sa = imp.getFactory().getDialect().getQuerySequencesString();
		List<String> sequences = entityManager.createNativeQuery(sa)
				.getResultList();
		for (String sequence : sequences) {
			if (sequence.toLowerCase().equals(sequenceName.toLowerCase())) {
				return;
			}
		}
		String sql = imp.getFactory().getDialect().getCreateSequenceStrings(
				sequenceName, initialValue, incrementSize)[0];
		entityManager.createNativeQuery(sql).executeUpdate();
	}
}
