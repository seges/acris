package sk.seges.acris.binding.client.providers.support;

import java.util.Date;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;
import sk.seges.acris.binding.client.providers.support.handlers.ValueChangeHandlerAdapter;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public abstract class AbstractBindingValueChangeHandlerAdapterProvider<M extends HasValueChangeHandlers<Date>, T> extends HandlerBindingAdapterProvider<M, T, ValueChangeHandler<Date>> {

	public static final String PROPERTY_VALUE = "value";
	
	protected abstract T getValue(M widget);
	protected abstract void setValue(M widget, T t);
	
	@SuppressWarnings("unchecked")
	protected BeanAdapter createHandlerAdapter(Object source, String property) {
		if (source instanceof HasValueChangeHandlers) {
			return new ValueChangeHandlerAdapter<M, T>((M)source, property, this);
		}
		throw new IllegalArgumentException("Source does not support value change events");
	}
	
	@Override
	public String getBindingWidgetProperty() {
		return PROPERTY_VALUE;
	}
}