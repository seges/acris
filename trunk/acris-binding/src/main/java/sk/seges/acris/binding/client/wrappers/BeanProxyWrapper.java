package sk.seges.acris.binding.client.wrappers;

import java.util.List;

import org.gwt.beansbinding.core.client.util.HasPropertyChangeSupport;

/**
 * <p>
 * Interface is currently used for One-To-Many binding. BeanProxyWrapper holds
 * target bean and provides setting/getting binding object using {@link List} of
 * proxy objects. If any new value is written, then matching proxy object is
 * found (matching mechanism is identify object by binding property) and
 * transparently set to the targetWrapper. If value is requested (using read
 * method), than is returned original object from targetWrapper.
 * </p>
 * <p>
 * BeanProxyWrapper is used for binding, but does not implement
 * {@link HasPropertyChangeSupport}, because of problems with Introspection
 * (Introspection was working good in development mode, but was not working in
 * deployment mode. After few fixes it was working in in deployment mode, but
 * stops working in development mode, so we were not able to provide reasonable
 * fix for this problem) and binding is done using Adapter. Binding mechanism is
 * register adapter provider in his own so there is not necessary to register
 * adapter provider by yourself in your code.
 * </p>
 * 
 * </p> Requested changes:
 * <ul>
 * <li>Enhance matching mechanism - match by identifier of the proxy object, not
 * by binding property</li>
 * </ul>
 * 
 * @author Peter Simun
 * 
 * @param <M>
 *            Type of the binding bean wrapped into the {@link BeanWrapper}.
 *            This bean is set using setTargetWrapper method. This is "source"
 *            object in the binding
 * @param <T>
 *            Type of the proxy objects. One of this proxy objects is bound in
 *            binding bean (targetWrapper) and all are displayed in binding
 *            widget (in this case ListBox)
 */
public interface BeanProxyWrapper<M, T> {

	/**
	 * Sets binding bean wrapped into the {@link BeanWrapper}. This wrapper
	 * should not be null and should not have null content.
	 */
	public void setTargetWrapper(BeanWrapper<M> targetBeanWrapper);

	/**
	 * Sets proxy objects for One-To-Many (BindingBean-To-ProxyObjects) purposes
	 */
	public void setProxyValues(List<T> proxyValues);

	/**
	 * Method is only internally used for binding purposes and should pass
	 * values from bean to widget
	 */
	public String getBoundPropertyValue();

	/**
	 * Method is only internally used for binding purposes and should pass value
	 * from widget to bean
	 */
	public void setBoundPropertyValue(String property);
}
