package sk.seges.acris.binding.client.bind.providers.support;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;

import sk.seges.acris.binding.client.bind.providers.support.generic.HandlerBindingAdapterProvider;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class AbstractBindingChangeHandlerAdapterProvider<M extends HasChangeHandlers, T> extends HandlerBindingAdapterProvider<M, T, ChangeHandler> {

	public static final String PROPERTY_VALUE = "value";
	
	protected abstract T getValue(M widget);
	protected abstract void setValue(M widget, T t);
	
	public static final class ChangeHandlerAdapter<M extends HasChangeHandlers, T> extends AbstractHandlerAdapter<M, T, ChangeHandler> {

		public ChangeHandlerAdapter(M handlerWidget, String property,
				HandlerBindingAdapterProvider<M, T, ChangeHandler> provider) {
			super(handlerWidget, property, provider);
		}

		protected HandlerRegistration addHandlerToWidget(ChangeHandler handler) {
			return widget.addChangeHandler((ChangeHandler)handler);
		}
		
		protected ChangeHandler createHandler() {
			return new Handler();
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
	protected BeanAdapter createHandlerAdapter(Object source, String property) {
		if (source instanceof HasChangeHandlers) {
			return new ChangeHandlerAdapter<M, T>((M)source, property, this);
		}
		throw new IllegalArgumentException("Source does not support change events");
	}
}