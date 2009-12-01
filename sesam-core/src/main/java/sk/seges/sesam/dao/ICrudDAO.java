/**
 * 
 */
package sk.seges.sesam.dao;

import sk.seges.sesam.domain.IDomainObject;



/**
 * @author eldzi
 */
public interface ICrudDAO<E extends IDomainObject<?>> extends IDataAccessObject, IFinderDAO<E> {
	E persist(E entity);
	E merge(E entity);
	void remove(E entity);
}
