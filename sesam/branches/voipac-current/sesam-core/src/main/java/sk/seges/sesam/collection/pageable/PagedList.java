/**
 * 
 */
package sk.seges.sesam.collection.pageable;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import sk.seges.sesam.dao.IDataLoader;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

/**
 * List fetching only required page of results. For fetching a data loader is used.
 * 
 * @param <E> Type of list
 * @author eldzi
 */
public class PagedList<E> extends AbstractPagedList<E> {
	private IDataLoader<List<E>> dataLoader;

	/** AbstractList.modCount. */
	protected transient int modCount = 0;
	
	public PagedList() {
	}

	public void setDataLoader(IDataLoader<List<E>> dataLoader) {
		this.dataLoader = dataLoader;
	}

	@Override
	protected final PagedResult<List<E>> getPagedResult() {
		if (pagedResult == null) {
			fetchPage(new Page(0, defaultPageSize));
		}
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
	 */
	private void fetchPage(Page page) {
		setPagedResult(dataLoader.load(page));
	}

	/**
	 * Returns element directly if it is within fetched page, else fetch a new
	 * page if available.
	 * 
	 * @see java.util.List#get(int)
	 */
	public E get(int index) throws IndexOutOfBoundsException {
		if (index < 0)
			throw new IndexOutOfBoundsException("Index (" + index
					+ ") shouldn't be less than 0");

		if (isOutOfCachedPagedResult(index)) {
			int nearestStartIndex = getNearestIndexToPageSize(index, defaultPageSize);
			fetchPage(new Page(nearestStartIndex, defaultPageSize));
//			System.out.println("fetch " + index);
		}

		if (index >= getPagedResult().getTotalResultCount())
			throw new IndexOutOfBoundsException("Index (" + index
					+ ") shouldn't be greater than total number of elements.");

		int fetchIndex = index % getPagedResult().getPage().getPageSize();
//		System.out.println("get " + index);
		return getPagedResult().getResult().get(fetchIndex);
	}

	/**
	 * Forces list to invalidate its current fetched page and in the next fetch
	 * round to lazily initialize requested one.
	 */
	public void reload() {
		setPagedResult(null);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {
		Object[] result = new Object[getPagedResult().getTotalResultCount()];
        for (int i = 0; i < getPagedResult().getTotalResultCount(); i++)
            result[i] = get(i);
        return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#toArray(T[])
	 */
	public <T> T[] toArray(T[] a) {
		if (a.length < getPagedResult().getTotalResultCount()) {
//            a = (T[])java.lang.reflect.Array.newInstance(
//                                a.getClass().getComponentType(), getPagedResult().getTotalResultCount());
			//FIXME: GWT don't know java.lang.reflect :(
			throw new IllegalStateException("Provided array of size " + a.length + " is smaller than requested count " + getPagedResult().getTotalResultCount());
		}
		
        Object[] result = a;
        for (int i = 0; i < getPagedResult().getTotalResultCount(); i++)
        	result[i] = get(i);

        if (a.length > getPagedResult().getTotalResultCount())
            a[getPagedResult().getTotalResultCount()] = null;

        return a;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.List#iterator()
	 */
	public Iterator<E> iterator() {
		return new Itr();
	}
	
	   private class Itr implements Iterator<E> {
		/**
		 * Index of element to be returned by subsequent call to next.
		 */
		int cursor = 0;

		/**
		 * Index of element returned by most recent call to next or
		 * previous.  Reset to -1 if this element is deleted by a call
		 * to remove.
		 */
		int lastRet = -1;

		/**
		 * The modCount value that the iterator believes that the backing
		 * List should have.  If this expectation is violated, the iterator
		 * has detected concurrent modification.
		 */
		int expectedModCount = modCount;

		public boolean hasNext() {
			return cursor != size();
		}

		public E next() {
			checkForComodification();
			try {
				E next = get(cursor);
				lastRet = cursor++;
				return next;
			} catch (IndexOutOfBoundsException e) {
				checkForComodification();
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			if (lastRet == -1)
				throw new IllegalStateException();
			checkForComodification();

			try {
				PagedList.this.remove(lastRet);
				if (lastRet < cursor)
					cursor--;
				lastRet = -1;
				expectedModCount = modCount;
			} catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}

		final void checkForComodification() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}
	}
	   
	@Override
	public int indexOf(Object o) {
		int index = getPagedResult().getResult().indexOf(o);
		if(index == -1) {
			throw new IllegalArgumentException("This object isn't currently fetched. Retrieving via data loader isn't implemented yet. Object o = " + o);
		}
		return index;
	}
}
