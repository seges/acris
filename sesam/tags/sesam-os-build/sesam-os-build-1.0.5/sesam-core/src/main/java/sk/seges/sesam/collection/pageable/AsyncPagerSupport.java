/**
 * 
 */
package sk.seges.sesam.collection.pageable;

import sk.seges.sesam.dao.ICallback;

/**
 * Pager support for asynchronous paging using asynchronous paged list.
 * @param <E> Type of list pager support is fetching
 * @author eldzi
 */
public class AsyncPagerSupport<E> extends AbstractPagerSupport<E> {
	private final ICallback<E> callback;
	
	public AsyncPagerSupport(AsyncPagedList<E> list, ICallback<E> callback) {
		super(list);
		this.callback = callback;
	}

	/* (non-Javadoc)
	 * @see sk.seges.sesam.collection.pageable.AbstractPagerSupport#fetch(int)
	 */
	@Override
	protected void fetch(int index) {
		((AsyncPagedList<E>)pagedList).get(index, callback);
	}

}
