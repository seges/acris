package sk.seges.acris.binding.client.providers.support.generic;

import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;

public interface IBindingBeanAdapterProvider<M> extends BeanAdapterProvider {
	public Class<M> getBindingWidgetClasses();

	public boolean isSupportSuperclass();
	
	public String getBindingWidgetProperty();
}