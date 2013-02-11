/**
 * 
 */
package sk.seges.sesam.domain;

import sk.seges.sesam.handler.ValueChangeHandler;

/**
 * @author eldzi
 */
public interface IObservableObject<T> {
	void addPropertyChangeListener(ValueChangeHandler<T> listener);
	void removePropertyChangeListener(ValueChangeHandler<T> listener);
}
