package sk.seges.acris.binding.client.providers.support.generic;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;

import sk.seges.acris.binding.client.providers.support.handlers.ChangeHandlerAdapter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public abstract class HandlerBindingAdapterProvider<M extends HasHandlers, T, V extends EventHandler> implements IBindingBeanAdapterProvider<M> {

	protected abstract T getValue(M widget);
	protected abstract void setValue(M widget, T t);
	
	public static abstract class AbstractHandlerAdapter<M extends HasHandlers, T, V extends EventHandler> extends BeanAdapterBase {
		
		protected M widget;		
		protected HandlerRegistration handlerRegistration;
		protected T previousValue;
		protected HandlerBindingAdapterProvider<M, T, V> provider;
		
		public AbstractHandlerAdapter(M handlerWidget, String property, HandlerBindingAdapterProvider<M, T, V> provider) {
			super(property);
			this.widget = handlerWidget;
			this.provider = provider;
		}

		public T getValue() {
			return provider.getValue(widget);
		}

		public void setValue(T t) {
			provider.setValue(widget, t);
		}

		@Override
		protected void listeningStarted() {
			V handler = createHandler();
			previousValue = getValue();
			handlerRegistration = addHandlerToWidget(handler);
		}

		protected abstract HandlerRegistration addHandlerToWidget(V handler);
		protected abstract V createHandler();
		
		@Override
		protected void listeningStopped() {
			if (handlerRegistration != null) {
				handlerRegistration.removeHandler();
				handlerRegistration = null;
			}
			previousValue = null;
		}

	}

	protected abstract BeanAdapter createHandlerAdapter(Object source, String property);

	public BeanAdapter createAdapter(Object source, String property) {
		if (!providesAdapter(source.getClass(), property)) {
			throw new IllegalArgumentException();
		}
		return createHandlerAdapter(source, property);
	}

	public boolean providesAdapter(Class<?> type, String property) {
		return isSupportedClass(type) && property.intern() == getBindingWidgetProperty();
	}

	public Class<?> getAdapterClass(Class<?> type) {
		return isSupportedClass(type) ? ChangeHandlerAdapter.class : null;
	}

	protected boolean isSupportedClass(Class<?> type) {
		if (type == getBindingWidgetClasses()) {
			return true;
		}
		
		if (isSupportSuperclass()) {
			type = type.getSuperclass();
			if (type != null && type != Object.class) {
				return isSupportedClass(type);
			}
		}
		return false;
	}
	
	@Override
	public boolean isSupportSuperclass() {
		return true;
	}
}