/**
 * 
 */
package sk.seges.acris.binding.client.providers;

import org.gwt.beansbinding.core.client.ConverterProvider;
import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasEnabled;

/**
 * @author ladislav.gazo
 */
@SuppressWarnings("unchecked")
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
			return ((HasEnabled) widget).isEnabled();
		}
		throw new RuntimeException("Neither FocusWidget nor HasEnabled was provided");
	}

	@Override
	protected void setValue(HasHandlers widget, Object t) {
		Object convertedValue = t == null ? false : ConverterProvider.defaultConvert(t, Boolean.class);
		
		if (convertedValue instanceof Boolean) {
			if (widget instanceof FocusWidget) {
				((FocusWidget) widget).setEnabled((Boolean) convertedValue);
				return;
			}
			if (widget instanceof HasEnabled) {
				((HasEnabled) widget).setEnabled((Boolean) convertedValue);
				return;
			}
		}

		throw new RuntimeException("Neither FocusWidget nor HasEnabled are able to consume "
				+ (t != null ? t.getClass() : "N/A") + " values = " + t);
	}

	@Override
	public Class getBindingWidgetClasses() {
		return FocusWidget.class;
	}

	@Override
	public Class<?> getAdapterClass(Class type) {
		return Adapter.class;
	}

	@Override
	public boolean providesAdapter(Class type, String property) {
		return isSupportedClass(type) && property.intern() == getBindingWidgetProperty();
	}

	protected boolean isSupportedClass(Class type) {
		if (type == FocusWidget.class) {
			return true;
		}

//		for (Class iface : type.getInterfaces()) {
//			if (iface == HasEnabled.class) {
//				return true;
//			}
//		}

		if (isSupportSuperclass()) {
			type = type.getSuperclass();
			if (type != null && type != Object.class) {
				return isSupportedClass(type);
			}
		}
		return false;
	}

	public static class Adapter extends BeanAdapterBase {
		private final HasHandlers widget;
		private final HasEnabledAdapterProvider provider;

		public Adapter(String property, HasHandlers widget, HasEnabledAdapterProvider provider) {
			super(property);
			this.widget = widget;
			this.provider = provider;
		}

		public Boolean isEnabled() {
			return (Boolean) provider.getValue(widget);
		}

		public void setEnabled(Boolean t) {
			provider.setValue(widget, t);
		}

	}
}
