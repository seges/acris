/**
 * 
 */
package sk.seges.sesam.domain;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.handler.ValueChangeHandler;

/**
 * Object used to hold a value. Value placed in value holder can change over
 * time and observer of value holder can be notified of this change.
 * 
 * @param <T>
 *            Type of value held.
 * @author eldzi
 * @since 15.12.2007
 */
public class ValueHolder<T> implements IObservableObject<T> {
	
	public static final String PROPERTY_VALUE="value";
	private T value;
	
	protected List<ValueChangeHandler<T>> handlers = new ArrayList<ValueChangeHandler<T>>();
	
	public ValueHolder() {}
	
	public ValueHolder(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		T oldValue = this.value;
		this.value = value;
		for (ValueChangeHandler<T> handler: handlers) {
			handler.onValueChanged(oldValue, value);
		}
	}

	/* (non-Javadoc)
	 * @see sk.seges.sesam.domain.IObservableObject#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(ValueChangeHandler<T> listener) {
		handlers.add(listener);
	}

	/* (non-Javadoc)
	 * @see sk.seges.sesam.domain.IObservableObject#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void removePropertyChangeListener(ValueChangeHandler<T> listener) {
		handlers.remove(listener);
	}	
}