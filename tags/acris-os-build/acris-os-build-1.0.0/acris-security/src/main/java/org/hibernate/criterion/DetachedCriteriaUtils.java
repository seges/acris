package org.hibernate.criterion;

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
public class DetachedCriteriaUtils {

    @SuppressWarnings("unchecked")
	public Class<IDomainObject<ISecuredObject>> getDetachedCriteriaDomainObjectClass(DetachedCriteria detachedCriteria) {

		String className = detachedCriteria.getCriteriaImpl().getEntityOrClassName();

		try {
			return (Class<IDomainObject<ISecuredObject>>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Entity support not implemented yet.");
		}
	}
}