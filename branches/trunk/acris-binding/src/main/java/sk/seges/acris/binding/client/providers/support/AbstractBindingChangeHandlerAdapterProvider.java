package sk.seges.acris.binding.client.providers.support;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;
import sk.seges.acris.binding.client.providers.support.handlers.ChangeHandlerAdapter;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;

public abstract class AbstractBindingChangeHandlerAdapterProvider<M extends HasChangeHandlers, T> extends HandlerBindingAdapterProvider<M, T, ChangeHandler> {

	public static final String PROPERTY_VALUE = "value";
	
	protected abstract T getValue(M widget);
	protected abstract void setValue(M widget, T t);
	
	@SuppressWarnings("unchecked")
	protected BeanAdapter createHandlerAdapter(Object source, String property) {
		if (source instanceof HasChangeHandlers) {
			return new ChangeHandlerAdapter<M, T>((M)source, property, this);
		}
		throw new IllegalArgumentException("Source does not support change events");
	}
	
	@Override
	public String getBindingWidgetProperty() {
		return PROPERTY_VALUE;
	}
}