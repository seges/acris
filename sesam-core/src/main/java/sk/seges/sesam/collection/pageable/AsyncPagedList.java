/**
 * 
 */
package sk.seges.sesam.collection.pageable;

import java.util.Iterator;
import java.util.List;

import sk.seges.sesam.dao.IAsyncDataLoader;
import sk.seges.sesam.dao.ICallback;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

/**
 * Asynchronous paged list with synchronous access to cached paged result.
 * 
 * @param <E> Type of list
 * 
 * @author eldzi
 */
public class AsyncPagedList<E> extends AbstractPagedList<E> {
	private static final String MSG_NOT_IMPLEMENTED_YET = "Not implemented yet";
	
	private IAsyncDataLoader<List<E>> dataLoader;

	public AsyncPagedList() {
	}

	public void setDataLoader(IAsyncDataLoader<List<E>> dataLoader) {
		this.dataLoader = dataLoader;
	}

	public void getPagedResult(final ICallback<PagedResult<List<E>>> callback) {
		if (pagedResult == null)
			fetchPage(new Page(0, defaultPageSize), new ICallback<Void>() {

				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				public void onSuccess(Void result) {
					callback.onSuccess(pagedResult);
				}
				
			});
		else
			callback.onSuccess(pagedResult);
	}
	
	/**
	 * Current cached paged result.
	 * @return
	 */
	public PagedResult<List<E>> getPagedResult() {
		return pagedResult;
	}

	/**
	 * This method is abstraction of fetching. If needed we can create an
	 * abstract parent class with this method as abstract and ResultHandlerModel
	 * will be DAOResultHandlerModel that will implement only this method and
	 * corresponding DAO injection.
	 * 
	 * In case of need of this higher abstraction we can easily divide this
	 * class.
	 * 
	 * @param page
	 *            A page that should be fetched.
	 * @param callback A callback indicating if fetching was successful.
	 */
	private void fetchPage(Page page, final ICallback<Void> callback) {
		dataLoader.load(page, new ICallback<PagedResult<List<E>>>() {

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess(PagedResult<List<E>> result) {
				setPagedResult(result);
				callback.onSuccess(null);
			}
		});
	}

	/**
	 * Returns element directly if it is within fetched (cached) page, else throw exception.
	 * 
	 * @see java.util.List#get(int)
	 */
	public E get(int index) throws IndexOutOfBoundsException {
		if (index < 0)
			throw new IndexOutOfBoundsException("Index (" + index
					+ ") shouldn't be less than 0");

		if (isOutOfCachedPagedResult(index)) {
			throw new IndexOutOfBoundsException(
					"Index should be within range <"
							+ getPagedResult().getPage().getStartIndex()
							+ " ; " + getEndIndex()
							+ ">");
		}

		int fetchIndex = index % getPagedResult().getPage().getPageSize();
//		System.out.println("get " + index);
		return getPagedResult().getResult().get(fetchIndex);
	}

	public void get(final int index, final ICallback<E> callback) throws IndexOutOfBoundsException {
		if (index < 0)
			callback.onFailure(new IndexOutOfBoundsException("Index (" + index
					+ ") shouldn't be less than 0"));

		if (isOutOfCachedPagedResult(index)) {
			int nearestStartIndex = getNearestIndexToPageSize(index, defaultPageSize);
			fetchPage(new Page(nearestStartIndex, defaultPageSize), new ICallback<Void>() {

				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				public void onSuccess(Void result) {
					callback.onSuccess(getFromLoadedPageResult(index));
//					System.out.println("fetch " + index);
				}
			});
		}

		if (index >= getPagedResult().getTotalResultCount())
			callback.onFailure(new IndexOutOfBoundsException("Index (" + index
					+ ") shouldn't be greater than total number of elements."));
		
		callback.onSuccess(getFromLoadedPageResult(index));		
	}
	
	private E getFromLoadedPageResult(int index) {
		int fetchIndex = index % getPagedResult().getPage().getPageSize();
//		System.out.println("get " + index);
		return getPagedResult().getResult().get(fetchIndex);
	}

	/**
	 * Forces list to invalidate its current fetched page and fetch again.
	 */
	public void reload(final ICallback<Void> callback) {
		Page page = (pagedResult == null ? new Page(0, defaultPageSize) : pagedResult.getPage());
		fetchPage(page, callback);
	}
	
	/**
	 * Forces list to invalidate based on provided page and fetch again.
	 */
	public void reload(final Page page, final ICallback<Void> callback) {
		fetchPage(page, callback);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {
		throw new RuntimeException(MSG_NOT_IMPLEMENTED_YET);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#toArray(T[])
	 */
	public <T> T[] toArray(T[] a) {
		throw new RuntimeException(MSG_NOT_IMPLEMENTED_YET);
	}

	public Iterator<E> iterator() {
		throw new RuntimeException(MSG_NOT_IMPLEMENTED_YET);
	}
}
