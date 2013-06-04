/**
 * 
 */
package sk.seges.sesam.collection.pageable;

import sk.seges.sesam.dao.Page;

/**
 * Support class for various pager implementations. All logic related to paging
 * and paged results has to be located here.
 * 
 * @author eldzi
 */
public abstract class AbstractPagerSupport {
	protected Page page;
	protected int resultSize;
	protected int totalCount;

	public void setValue(Page page, int resultSize, int totalCount) {
		this.page = page;
		this.resultSize = resultSize;
		this.totalCount = totalCount;
	}
	
	/**
	 * Method responsible for fetching specified index. Usually a get method
	 * with callback is a default implementation.
	 * 
	 * @param index
	 *            Provided (yet calculated) starting index of page that should
	 *            be fetched.
	 */
	protected abstract void fetch(Page page);

	/**
	 * Go to next page.
	 */
	public void next() {
		int currentIndex = page.getStartIndex();
		fetch(new Page(currentIndex + resultSize, page.getPageSize()));
	}

	/**
	 * Go to previous page.
	 */
	public void previous() {
		int currentIndex = page.getStartIndex();
		currentIndex -= page.getPageSize();
		currentIndex = (currentIndex < 0 ? 0 : currentIndex);
		fetch(new Page(currentIndex, page.getPageSize()));
	}

	/**
	 * Go to first page.
	 */
	public void first() {
		fetch(new Page(0, page.getPageSize()));
	}

	/**
	 * Go to last page.
	 */
	public void last() {
		int pageSize = page.getPageSize();
		int lastIndex = totalCount - 1;
		lastIndex = PagedList.getNearestIndexToPageSize(lastIndex, pageSize);
		fetch(new Page(lastIndex, page.getPageSize()));
	}

	/**
	 * @return True if results are from the last available page.
	 */
	public boolean isOnLastPage() {
		int pageSize = page.getPageSize();
		int lastIndex = totalCount - 1;
		return PagedList.getNearestIndexToPageSize(lastIndex, pageSize) == page.getStartIndex();
	}

	/**
	 * @return True if results are from the first available page.
	 */
	public boolean isOnFirstPage() {
		return page.getStartIndex() == 0;
	}
	
	public int getCurrentPageNumber() {
		return PagedList.getCurrentPageNumber(page);
	}
	
	public int getLastPageNumber() {
		return PagedList.getLastPageNumber(totalCount, page.getPageSize());
	}
}
