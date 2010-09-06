package sk.seges.acris.mvp.client.adapter.provider.smartgwt.core;

import org.gwt.beansbinding.core.client.ext.BeanAdapter;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;

import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.HasChangedHandlers;

public abstract class AbstractBindingSmartChangedHandlerAdapterProvider<M extends HasChangedHandlers, T> extends
		HandlerBindingAdapterProvider<M, T, ChangedHandler> {

	public static final String PROPERTY_VALUE = "value";

	protected abstract T getValue(M widget);

	protected abstract void setValue(M widget, T t);

	@SuppressWarnings("unchecked")
	protected BeanAdapter createHandlerAdapter(Object source, String property) {
		if (source instanceof HasChangedHandlers) {
			return new ChangedHandlerAdapter<M, T>((M) source, property, this);
		}
		throw new IllegalArgumentException("Source does not support change events");
	}
}