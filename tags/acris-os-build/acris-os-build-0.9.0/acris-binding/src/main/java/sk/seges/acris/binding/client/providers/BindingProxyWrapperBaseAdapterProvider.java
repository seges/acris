package sk.seges.acris.binding.client.providers;

import sk.seges.acris.binding.client.providers.support.generic.AbstractBindingBeanAdapterProvider;
import sk.seges.acris.binding.client.wrappers.BeanProxyWrapper;

public final class BindingProxyWrapperBaseAdapterProvider extends AbstractBindingBeanAdapterProvider<String> {

	@Override
	protected String getValue(BeanProxyWrapper<?, ?> widget) {
		return widget.getBoundPropertyValue();
	}

	@Override
	protected void setValue(BeanProxyWrapper<?, ?> widget, String t) {
		widget.setBoundPropertyValue(t);
	}
}