/**
 * 
 */
package sk.seges.sesam.collection.pageable;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.handler.ValueChangeHandler;

/**
 * Asynchronous paged list observable for paged result changes (e.g. new page was fetched...).
 * 
 * @param <E> Type of list
 * @author eldzi
 */
public class AsyncObservablePagedList<E> extends AsyncPagedList<E> {

	private List<ValueChangeHandler<PagedResult<?>>> valueChangeHandlers = new ArrayList<ValueChangeHandler<PagedResult<?>>>();
	
	@Override
	protected void setPagedResult(PagedResult<List<E>> pagedResult) {
		PagedResult<List<E>> oldValue = this.pagedResult;
		super.setPagedResult(pagedResult);
		
		for (ValueChangeHandler<PagedResult<?>> valueChangeHandler: valueChangeHandlers) {
			valueChangeHandler.onValueChanged(oldValue, pagedResult);
		}
		
	}

	/* (non-Javadoc)
	 * @see sk.seges.sesam.domain.IObservableObject#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(ValueChangeHandler<PagedResult<?>> listener) {
		valueChangeHandlers.add(listener);
	}

	/* (non-Javadoc)
	 * @see sk.seges.sesam.domain.IObservableObject#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(ValueChangeHandler<PagedResult<?>> listener) {
		valueChangeHandlers.remove(listener);
	}
}