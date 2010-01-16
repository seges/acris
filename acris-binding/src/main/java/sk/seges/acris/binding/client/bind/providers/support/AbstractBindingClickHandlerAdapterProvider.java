package sk.seges.acris.binding.client.bind.providers.support;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;

import sk.seges.acris.binding.client.bind.providers.support.generic.HandlerBindingAdapterProvider;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class AbstractBindingClickHandlerAdapterProvider<M extends HasClickHandlers, T> extends HandlerBindingAdapterProvider<M, T, ClickHandler> {

	public static final String PROPERTY_VALUE = "value";
	
	protected abstract T getValue(M widget);
	protected abstract void setValue(M widget, T t);
	
	public static final class ClickHandlerAdapter<M extends HasClickHandlers, T> extends AbstractHandlerAdapter<M, T, ClickHandler> {
		
		public ClickHandlerAdapter(M handlerWidget, String property,
				HandlerBindingAdapterProvider<M, T, ClickHandler> provider) {
			super(handlerWidget, property, provider);
		}

		protected HandlerRegistration addHandlerToWidget(ClickHandler handler) {
			return widget.addClickHandler(handler);
		}
		
		protected ClickHandler createHandler() {
			return new Handler();
		}

		private class Handler implements ClickHandler {
			public void onClick(ClickEvent event) {
				Object oldElementOrElements = previousValue;
				previousValue = getValue();
				firePropertyChange(oldElementOrElements, previousValue);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected BeanAdapter createHandlerAdapter(Object source, String property) {
		if (source instanceof HasClickHandlers) {
			return new ClickHandlerAdapter<M, T>((M)source, property, this);
		}
		throw new IllegalArgumentException("Source does not support click events");
	}
}