/**
 * 
 */
package sk.seges.acris.binding.client.providers;

import org.gwt.beansbinding.core.client.ConverterProvider;
import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;
import sk.seges.acris.binding.client.providers.support.widget.HasVisible;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.UIObject;

/**
 * @author ladislav.gazo
 */
@SuppressWarnings("unchecked")
public class HasVisibleAdapterProvider extends HandlerBindingAdapterProvider {
	private static final String VISIBLE = "visible";

	@Override
	public String getBindingWidgetProperty() {
		return VISIBLE;
	}

	@Override
	protected BeanAdapter createHandlerAdapter(Object source, String property) {
		return new Adapter(property, (HasHandlers) source, this);
	}

	@Override
	protected Object getValue(HasHandlers widget) {
		if (widget instanceof UIObject) {
			return ((UIObject) widget).isVisible();
		}
		if (widget instanceof HasVisible) {
			return ((HasVisible) widget).isVisible();
		}
		throw new RuntimeException("Neither UIObject nor HasEnabled was provided");
	}

	@Override
	protected void setValue(HasHandlers widget, Object t) {
		Object convertedValue = t == null ? false : ConverterProvider.defaultConvert(t, Boolean.class);
		
		if (convertedValue instanceof Boolean) {
			if (widget instanceof UIObject) {
				((UIObject) widget).setVisible((Boolean) convertedValue);
				return;
			}
			if (widget instanceof HasVisible) {
				((HasVisible) widget).setVisible((Boolean) convertedValue);
				return;
			}
		}

		throw new RuntimeException("Neither UIObject nor HasEnabled are able to consume "
				+ (t != null ? t.getClass() : "N/A") + " values = " + t);
	}

	@Override
	public Class getBindingWidgetClasses() {
		return UIObject.class;
	}

	@Override
	public Class<?> getAdapterClass(Class type) {
		return Adapter.class;
	}

	public boolean providesAdapter(Class type, String property) {
		return isSupportedClass(type) && property.intern() == getBindingWidgetProperty();
	}

	protected boolean isSupportedClass(Class type) {
		if (type == UIObject.class) {
			return true;
		}

//		for (Class iface : type.getInterfaces()) {
//			if (iface == HasVisible.class) {
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
		private final HasVisibleAdapterProvider provider;

		public Adapter(String property, HasHandlers widget, HasVisibleAdapterProvider provider) {
			super(property);
			this.widget = widget;
			this.provider = provider;
		}

		public Boolean isVisible() {
			return (Boolean) provider.getValue(widget);
		}

		public void setVisible(Boolean t) {
			provider.setValue(widget, t);
		}

	}
}
