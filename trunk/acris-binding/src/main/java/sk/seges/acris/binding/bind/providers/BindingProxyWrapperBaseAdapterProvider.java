package sk.seges.acris.binding.bind.providers;

import sk.seges.acris.binding.bind.BeanProxyWrapper;
import sk.seges.acris.binding.bind.providers.support.generic.AbstractBindingBeanAdapterProvider;

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