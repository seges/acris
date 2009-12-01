package sk.seges.acris.bind.providers.support;

import org.gwt.beansbinding.core.client.ext.BeanAdapterProvider;

public interface BindingBeanAdapterProvider<M> extends BeanAdapterProvider {
	public Class<M> getBindingWidgetClasses();

	public boolean isSupportSuperclass();
	
	public String getBindingWidgetProperty();
}