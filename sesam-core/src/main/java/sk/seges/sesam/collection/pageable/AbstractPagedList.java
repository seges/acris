package sk.seges.sesam.collection.pageable;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

/**
 * Base for paged list providing shared fields. Also contains not yet
 * implemented methods required by {@link List} interface.
 * 
 * @param <E> Type of list
 *  
 * @author eldzi
 */
public abstract class AbstractPagedList<E> implements List<E> {
	protected int defaultPageSize = 10;
	protected PagedResult<List<E>> pagedResult;

	/**
	 * @param defaultPageSize
	 *            the defaultPageSize to set
	 */
	public void setDefaultPageSize(int defaultPageSize) {
		this.defaultPageSize = defaultPageSize;
	}

	/**
	 * Return total number of available elements. Doesn't return number of
	 * elements within already fetched page.
	 * 
	 * @see java.util.List#size()
	 */
	public int size() {
		return getPagedResult().getTotalResultCount();
	}

	/**
	 * @return Index of the last result in cached paged results.
	 */
	protected int getEndIndex() {
		return pagedResult.getPage().getStartIndex()
				+ pagedResult.getResult().size() - 1;
	}

	protected void setPagedResult(PagedResult<List<E>> pagedResult) {
		this.pagedResult = pagedResult;
	}

	/**
	 * @return A page for current paged results. Don't modify it !
	 */
	public final Page getCurrentPage() {
		return pagedResult.getPage();
	}
	
	/**
	 * @return Currently loaded number of results in paged result.
	 */
	public int getCurrentResultsCount() {
		return pagedResult == null ? 0 : pagedResult.getResult().size();
	}
	
	/**
	 * @param requestedIndex
	 *            Index of a result for which nearest index in page has to be
	 *            calculated.
	 * @param pageSize
	 *            Page size used for calculated.
	 * @return Index of the first result in a page where requested index
	 *         belongs.
	 */
	public int getNearestIndexToPageSize(int requestedIndex, int pageSize) {
		int page = requestedIndex / pageSize;
		return page * pageSize;
	}

	/**
	 * Counted against total number of results available in a data source.
	 * Counting starts from 1 not 0.
	 * 
	 * @return A page number of current loaded paged results.
	 */
	public int getCurrentPageNumber() {
		return pagedResult.getPage().getStartIndex()
				/ pagedResult.getPage().getPageSize() + 1;
	}

	/**
	 * Respects counting starting from 1 not 0.
	 * 
	 * @return Last page number of total results available in a data source.
	 */
	public int getLastPageNumber() {
		return (pagedResult.getTotalResultCount() - 1)
				/ pagedResult.getPage().getPageSize() + 1;
	}

	/**
	 * @param index
	 *            Index of requested result within range of total available
	 *            number of results in data source.
	 * @return True if index is out of cached paged result range.
	 */
	protected boolean isOutOfCachedPagedResult(int index) {
		return !(index >= getPagedResult().getPage().getStartIndex() && index <= getEndIndex());
	}

	protected abstract PagedResult<List<E>> getPagedResult();

	/* NOT YET IMPLEMENTED part */

	private List<E> list = null;

	/*
	 * **************************************************
	 * 
	 * ********** DELEGATES not rewritten ***************
	 * 
	 * **************************************************
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(E e) {
		return list.add(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#add(int, java.lang.Object)
	 */

	public void add(int index, E element) {
		list.add(index, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#addAll(java.util.Collection)
	 */

	public boolean addAll(Collection<? extends E> c) {
		return list.addAll(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */

	public boolean addAll(int index, Collection<? extends E> c) {
		return list.addAll(index, c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#clear()
	 */

	public void clear() {
		list.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#contains(java.lang.Object)
	 */

	public boolean contains(Object o) {
		return list.contains(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#containsAll(java.util.Collection)
	 */

	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#indexOf(java.lang.Object)
	 */

	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#isEmpty()
	 */

	public boolean isEmpty() {
		return list.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */

	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#listIterator()
	 */

	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#listIterator(int)
	 */

	public ListIterator<E> listIterator(int index) {
		return list.listIterator(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#remove(java.lang.Object)
	 */

	public boolean remove(Object o) {
		return list.remove(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#remove(int)
	 */

	public E remove(int index) {
		return list.remove(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#removeAll(java.util.Collection)
	 */

	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#retainAll(java.util.Collection)
	 */

	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#set(int, java.lang.Object)
	 */

	public E set(int index, E element) {
		return list.set(index, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#subList(int, int)
	 */

	public List<E> subList(int fromIndex, int toIndex) {
//		return list.subList(fromIndex, toIndex);
		throw new RuntimeException("Not implemented at all. not delegated because GWT 1.5.0-rc1");
	}
}
