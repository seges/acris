/**
 * 
 */
package sk.seges.sesam.domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Object used to hold a value. Value placed in value holder can change over
 * time and observer of value holder can be notified of this change.
 * 
 * @param <T>
 *            Type of value held.
 * @author eldzi
 * @since 15.12.2007
 */
public class ValueHolder<T> implements IObservableObject {
	public static final String PROPERTY_VALUE="value";
	private T value;
	
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
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
		pcs.firePropertyChange(PROPERTY_VALUE, oldValue, value);
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
		pcs.addPropertyChangeListener(listener);
	}
	
}
