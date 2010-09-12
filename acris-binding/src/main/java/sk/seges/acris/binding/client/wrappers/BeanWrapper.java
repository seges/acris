/**
 * 
 */
package sk.seges.acris.binding.client.wrappers;

import sk.seges.sesam.domain.IObservableObject;

/**
 * <p>
 * BeanWrapper wraps existing bean and ensures that we can provide dynamic
 * accessing of the properties via Introspector. All property changes also fires
 * propertyChange listeners. See {@link IObservableObject} for further details.
 * </p>
 * <p>
 * Bean wrapper is mostly used transparently by generators in binding mechanism
 * in the way that whenever user sets binding bean to the binding form, it is
 * automatically wrapped by bean wrapper and used by beans-binding.
 * </p>
 * <p>
 * Provides transparent reflection mechanism in GWT and is part of the
 * {@link Introspection} while resolving beanProperty in the "runtime"
 * </p>
 * 
 * @author eldzi
 */
public interface BeanWrapper<T> extends IObservableObject {
	public static final String CONTENT = "beanWrapperContent";
	
	/**
	 * Used for setting wrapped content. BeanWrapper delegates all get and set
	 * method requests to this instance. If no value is set, than
	 * {@link NullPointerException} are thrown because bean wrapper over the
	 * null object is not valid.
	 */
	void setBeanWrapperContent(T content);

	/**
	 * User can obtain original bean from bean wrapper with set values using
	 * this method.
	 */
	T getBeanWrapperContent();

	/**
	 * Method will delegate request to read method for appropriate property in
	 * wrapped bean
	 * 
	 * <pre>
	 * @code getAttribute("name")}
	 * </pre>
	 * 
	 * will call:
	 * 
	 * <pre>
	 * @code content.getName();}
	 * </pre>
	 */
	Object getBeanAttribute(String attr);

	/**
	 * Method will delegate request to write method for appropriate property in
	 * wrapped bean.
	 * 
	 * <pre>
	 * @code setBeanAttribute("name","test")}
	 * </pre>
	 * 
	 * will call:
	 * 
	 * <pre>
	 * @code content.setName("test");}
	 * </pre>
	 */
	void setBeanAttribute(String attr, Object value);
}
