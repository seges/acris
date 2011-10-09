/**
 * 
 */
package sk.seges.sesam.collection.pageable;

import sk.seges.sesam.dao.ICallback;
import sk.seges.sesam.dao.Page;

/**
 * Pager support for asynchronous paging using asynchronous paged list.
 * @param <E> Type of list pager support is fetching
 * @author eldzi
 */
public class AsyncPagerSupport<E> extends AbstractPagerSupport {
	private final ICallback<E> callback;
	private final AsyncPagedList<E> list;
	
	public AsyncPagerSupport(AsyncPagedList<E> list, ICallback<E> callback) {
		this.list = list;
		this.callback = callback;
		
		
	}

	/* (non-Javadoc)
	 * @see sk.seges.sesam.collection.pageable.AbstractPagerSupport#fetch(Page)
	 */
	@Override
	protected void fetch(Page page) {
		((AsyncPagedList<E>)list).get(page.getStartIndex(), callback);
	}

}
