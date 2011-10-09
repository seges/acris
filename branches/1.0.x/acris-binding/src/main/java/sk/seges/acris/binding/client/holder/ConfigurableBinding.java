/**
 * 
 */
package sk.seges.acris.binding.client.holder;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;


/**
 * @author ladislav.gazo
 */
public interface ConfigurableBinding<T> {
	IBindingHolder<T> getHolder();
	BeanWrapper<T> getBeanWrapper();
}
