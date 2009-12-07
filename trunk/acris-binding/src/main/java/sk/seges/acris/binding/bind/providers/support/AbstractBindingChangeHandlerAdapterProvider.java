package sk.seges.acris.binding.bind.providers.support;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;
import org.gwt.beansbinding.ui.client.adapters.BeanAdapterBase;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class AbstractBindingChangeHandlerAdapterProvider<M extends HasChangeHandlers, T> implements BindingBeanAdapterProvider<M> {

	public static final String PROPERTY_VALUE = "value";
	
	protected abstract T getValue(M widget);
	protected abstract void setValue(M widget, T t);
	
	public static final class ChangeHandlerAdapter<M extends HasChangeHandlers, T> extends BeanAdapterBase {
		
		private M widget;		
		private HandlerRegistration handlerRegistration;
		private T previousValue;
		private AbstractBindingChangeHandlerAdapterProvider<M, T> provider;
		
		public ChangeHandlerAdapter(M changeHandlerWidget, String property, AbstractBindingChangeHandlerAdapterProvider<M, T> provider) {
			super(property);
			this.widget = changeHandlerWidget;
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
			Handler handler = new Handler();
			previousValue = getValue();
			handlerRegistration = widget.addChangeHandler(handler);
		}

		@Override
		protected void listeningStopped() {
			if (handlerRegistration != null) {
				handlerRegistration.removeHandler();
				handlerRegistration = null;
			}
			previousValue = null;
		}

		private class Handler implements ChangeHandler {
			public void onChange(ChangeEvent arg0) {
				Object oldElementOrElements = previousValue;
				previousValue = getValue();
				firePropertyChange(oldElementOrElements, previousValue);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public BeanAdapter createAdapter(Object source, String property) {
		if (!providesAdapter(source.getClass(), property)) {
			throw new IllegalArgumentException();
		}
		
		if (source instanceof HasChangeHandlers) {
			//Check this
			//M m = getBindingWidgetClasses().cast(source);
			return new ChangeHandlerAdapter<M, T>((M)source, property, this);
		}
		throw new IllegalArgumentException("Source does not support change events");
	}

	public boolean providesAdapter(Class<?> type, String property) {
		return isSupportedClass(type) && property.intern() == getBindingWidgetProperty();
	}

	public Class<?> getAdapterClass(Class<?> type) {
		return isSupportedClass(type) ? ChangeHandlerAdapter.class : null;
	}

	public String getBindingWidgetProperty() {
		return PROPERTY_VALUE;
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