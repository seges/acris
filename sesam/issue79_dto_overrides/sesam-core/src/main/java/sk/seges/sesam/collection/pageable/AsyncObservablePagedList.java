/**
 * 
 */
package sk.seges.sesam.collection.pageable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import sk.seges.sesam.dao.PagedResult;

/**
 * Asynchronous paged list observable for paged result changes (e.g. new page was fetched...).
 * 
 * @param <E> Type of list
 * @author eldzi
 */
public class AsyncObservablePagedList<E> extends AsyncPagedList<E> {
	public static final String PROPERTY_PAGED_RESULT = "pagedResult";
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	@Override
	protected void setPagedResult(PagedResult<List<E>> pagedResult) {
		PagedResult<List<E>> oldValue = this.pagedResult;
		super.setPagedResult(pagedResult);
		pcs.firePropertyChange(PROPERTY_PAGED_RESULT, oldValue, pagedResult);
		
	}

	/* (non-Javadoc)
	 * @see sk.seges.sesam.domain.IObservableObject#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	/* (non-Javadoc)
	 * @see sk.seges.sesam.domain.IObservableObject#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
}
