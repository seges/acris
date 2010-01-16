package org.hibernate.criterion;

public class DetachedCriteriaProcessor {
	public Class getEntityClassForDetachedCriteria(DetachedCriteria detachedCriteria) {
		String entityOrClassName = detachedCriteria.getCriteriaImpl().getEntityOrClassName();
		
		try {
			return Class.forName(entityOrClassName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Entities are not supported for now.");
		}
	}
}
