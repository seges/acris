/**
 * 
 */
package sk.seges.acris.binding.client.providers;

import org.gwt.beansbinding.core.client.ConverterProvider;
import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;
import sk.seges.acris.binding.client.providers.support.handlers.ChangeHandlerAdapter;
import sk.seges.acris.binding.client.providers.support.widget.HasEnabled;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.FocusWidget;

/**
 * @author ladislav.gazo
 */
public class HasEnabledAdapterProvider extends HandlerBindingAdapterProvider {
	private static final String ENABLED = "enabled";

	@Override
	public String getBindingWidgetProperty() {
		return ENABLED;
	}

	@Override
	protected BeanAdapter createHandlerAdapter(Object source, String property) {
		return new Adapter(property, (HasHandlers) source, this);
	}

	@Override
	protected Object getValue(HasHandlers widget) {
		if (widget instanceof FocusWidget) {
			return ((FocusWidget) widget).isEnabled();
		}
		if (widget instanceof HasEnabled) {
			return ((FocusWidget) widget).isEnabled();
		}
		throw new RuntimeException("Neither FocusWidget nor HasEnabled was provided");
	}

	@Override
	protected void setValue(HasHandlers widget, Object t) {
		Object convertedValue = t == null ? true : ConverterProvider.defaultConvert(t, Boolean.class);
		if (convertedValue instanceof Boolean) {
			if (widget instanceof FocusWidget) {
				((FocusWidget) widget).setEnabled((Boolean) convertedValue);
			}
			if (widget instanceof HasEnabled) {
				((HasEnabled) widget).setEnabled((Boolean) convertedValue);
			}
		}

		throw new RuntimeException("Neither FocusWidget nor HasEnabled are able to consume " + t.getClass()
				+ " values = " + t);
	}

	@Override
	public Class getBindingWidgetClasses() {
		return FocusWidget.class;
	}

	@Override
	public Class<?> getAdapterClass(Class type) {
		return Adapter.class;
	}

	public boolean providesAdapter(Class type, String property) {
		return isSupportedClass(type) && property.intern() == getBindingWidgetProperty();
	}

	protected boolean isSupportedClass(Class type) {
		if (type == FocusWidget.class) {
			return true;
		}
		
		for(Class iface : type.getInterfaces()) {
			if(iface == HasEnabled.class) {
				return true;
			}
		}
		
		if (isSupportSuperclass()) {
			type = type.getSuperclass();
			if (type != null && type != Object.class) {
				return isSupportedClass(type);
			}
		}
		return false;
	}
	
	private static class Adapter extends BeanAdapterBase {
		private final HasHandlers widget;
		private final HasEnabledAdapterProvider provider;

		public Adapter(String property, HasHandlers widget, HasEnabledAdapterProvider provider) {
			super(property);
			this.widget = widget;
			this.provider = provider;
		}

		public Object getValue() {
			return provider.getValue(widget);
		}

		public void setValue(Object t) {
			provider.setValue(widget, t);
		}

		
	}
}
