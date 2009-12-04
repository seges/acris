package sk.seges.acris.bind.providers;

import sk.seges.acris.bind.BeanProxyWrapper;
import sk.seges.acris.bind.providers.support.AbstractBindingBeanAdapterProvider;

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