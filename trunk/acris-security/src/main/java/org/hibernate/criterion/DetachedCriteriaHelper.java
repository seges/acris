package org.hibernate.criterion;

import java.lang.reflect.Type;

import sk.seges.acris.security.rpc.domain.ISecuredObject;
import sk.seges.sesam.domain.IDomainObject;

/**
 * <pre>
 * Helper class over detached criteria in order to provide criteria specific
 * attributes which has package visibility. Used for ACL detached criteria
 * injector. Only DetachedCriteria.forClass(Class) is supported!
 * DetachedCriteria.forEntity(String) is not supported because ALC injector
 * needs java class because of ACL implementation.
 * </pre>
 * 
 * <pre>
 * When necessary you can add entity based DetachedCriteria support by
 * transforming entity into correct java class.
 * </pre>
 * 
 * @author fat
 */
public class DetachedCriteriaHelper {

	public Class<IDomainObject<ISecuredObject>> getDetachedCriteriaDomainObjectClass(
			DetachedCriteria detachedCriteria) {

		// resolving class name of entity
		String entityOrClassName = detachedCriteria.getCriteriaImpl()
				.getEntityOrClassName();

		// TODO Do the check if the generic class implements ISecuredObject
		try {
			Class<?> clazz = Class.forName(entityOrClassName);
			return (Class<IDomainObject<ISecuredObject>>) clazz;
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(
					"Entities are not supported for now.");
		}
	}
}