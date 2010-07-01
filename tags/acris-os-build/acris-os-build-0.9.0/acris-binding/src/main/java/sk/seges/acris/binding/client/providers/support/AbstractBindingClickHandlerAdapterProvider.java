package sk.seges.acris.binding.client.providers.support;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;
import sk.seges.acris.binding.client.providers.support.handlers.ClickHandlerAdapter;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public abstract class AbstractBindingClickHandlerAdapterProvider<M extends HasClickHandlers, T> extends HandlerBindingAdapterProvider<M, T, ClickHandler> {

	public static final String PROPERTY_VALUE = "value";
	
	protected abstract T getValue(M widget);
	protected abstract void setValue(M widget, T t);
	
	@SuppressWarnings("unchecked")
	protected BeanAdapter createHandlerAdapter(Object source, String property) {
		if (source instanceof HasClickHandlers) {
			return new ClickHandlerAdapter<M, T>((M)source, property, this);
		}
		throw new IllegalArgumentException("Source does not support click events");
	}
}