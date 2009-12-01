/**
 * 
 */
package sk.seges.sesam.collection.pageable;

/**
 * Support class for various pager implementations. All logic related to paging
 * and paged results has to be located here.
 * 
 * @param <E> Type of list this pager is paging.
 * 
 * @author eldzi
 */
public abstract class AbstractPagerSupport<E> {
	protected final AbstractPagedList<E> pagedList;

	public AbstractPagerSupport(AbstractPagedList<E> pagedList) {
		this.pagedList = pagedList;
	}

	/**
	 * Method responsible for fetching specified index. Usually a get method
	 * with callback is a default implementation.
	 * 
	 * @param index
	 *            Provided (yet calculated) starting index of page that should
	 *            be fetched.
	 */
	protected abstract void fetch(int index);

	/**
	 * Go to next page.
	 */
	public void next() {
		int size = pagedList.getPagedResult().getResult().size();
		int currentIndex = pagedList.getPagedResult().getPage().getStartIndex();
		fetch(currentIndex + size);
	}

	/**
	 * Go to previous page.
	 */
	public void previous() {
		int currentIndex = pagedList.getPagedResult().getPage().getStartIndex();
		currentIndex -= pagedList.getPagedResult().getPage().getPageSize();
		currentIndex = (currentIndex < 0 ? 0 : currentIndex);
		fetch(currentIndex);
	}

	/**
	 * Go to first page.
	 */
	public void first() {
		fetch(0);
	}

	/**
	 * Go to last page.
	 */
	public void last() {
		int pageSize = pagedList.getPagedResult().getPage().getPageSize();
		int lastIndex = pagedList.getPagedResult().getTotalResultCount() - 1;
		lastIndex = pagedList.getNearestIndexToPageSize(lastIndex, pageSize);
		fetch(lastIndex);
	}

	/**
	 * @return True if results are from the last available page.
	 */
	public boolean isOnLastPage() {
		int pageSize = pagedList.getPagedResult().getPage().getPageSize();
		int lastIndex = pagedList.getPagedResult().getTotalResultCount() - 1;
		return pagedList.getNearestIndexToPageSize(lastIndex, pageSize) == pagedList
				.getPagedResult().getPage().getStartIndex();
	}

	/**
	 * @return True if results are from the first available page.
	 */
	public boolean isOnFirstPage() {
		return pagedList.getPagedResult().getPage().getStartIndex() == 0;
	}
}
