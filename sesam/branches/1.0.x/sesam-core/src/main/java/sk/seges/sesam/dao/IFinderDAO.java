/**
 * 
 */
package sk.seges.sesam.dao;

import java.util.List;

/**
 * Interface for DAO that should implement basic set of finders required for
 * common operations (find all, find based on filter or sort criteria,...).
 * 
 * @author eldzi
 * @since 24.10.2007
 */
public interface IFinderDAO<E> extends IDataAccessObject {
	/**
	 * Search for all elements according to specified page.
	 * 
	 * @param requestedPage
	 *            Required page of elements. Number of elements is determined by
	 *            the specified size in requested page. Also a starting index
	 *            should be taken into account.
	 * @return Paged result list of requested elements.
	 */
	PagedResult<List<E>> findAll(Page requestedPage);

	/**
	 * Defines a way how to find entity in data source based on some entity information provided.
	 * 
	 * @param entity Filled with necessary information to find entity.
	 * @return Entity in data source.
	 */
	E findEntity(E entity);
}
