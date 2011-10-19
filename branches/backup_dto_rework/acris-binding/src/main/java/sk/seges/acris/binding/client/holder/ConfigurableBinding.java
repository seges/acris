/**
 * 
 */
package sk.seges.acris.binding.client.holder;

import java.io.Serializable;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;


/**
 * @author ladislav.gazo
 */
public interface ConfigurableBinding<T extends Serializable> {
	IBindingHolder<T> getHolder();
	BeanWrapper<T> getBeanWrapper();
}
