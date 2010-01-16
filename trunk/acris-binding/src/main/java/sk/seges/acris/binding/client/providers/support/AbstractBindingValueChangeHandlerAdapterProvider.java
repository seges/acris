package sk.seges.acris.binding.client.providers.support;

import java.util.Date;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public abstract class AbstractBindingValueChangeHandlerAdapterProvider<M extends HasValueChangeHandlers<Date>, T> extends HandlerBindingAdapterProvider<M, T, ValueChangeHandler<Date>> {

	public static final String PROPERTY_VALUE = "value";
	
	protected abstract T getValue(M widget);
	protected abstract void setValue(M widget, T t);
	
	public static final class ValueChangeHandlerAdapter<M extends HasValueChangeHandlers<Date>, T> extends AbstractHandlerAdapter<M, T, ValueChangeHandler<Date>> {

		public ValueChangeHandlerAdapter(M handlerWidget, String property,
				HandlerBindingAdapterProvider<M, T, ValueChangeHandler<Date>> provider) {
			super(handlerWidget, property, provider);
		}

		protected HandlerRegistration addHandlerToWidget(ValueChangeHandler<Date> handler) {
			return widget.addValueChangeHandler(handler);
		}
		
		protected ValueChangeHandler<Date> createHandler() {
			return new Handler();
		}

		private class Handler implements ValueChangeHandler<Date> {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				Object oldElementOrElements = previousValue;
				previousValue = getValue();
				firePropertyChange(oldElementOrElements, previousValue);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected BeanAdapter createHandlerAdapter(Object source, String property) {
		if (source instanceof HasValueChangeHandlers) {
			return new ValueChangeHandlerAdapter<M, T>((M)source, property, this);
		}
		throw new IllegalArgumentException("Source does not support value change events");
	}
}