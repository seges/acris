/**
 * 
 */
package sk.seges.acris.binding.client.providers.support.handlers;

import java.util.Date;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;
import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider.AbstractHandlerAdapter;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public final class ValueChangeHandlerAdapter<M extends HasValueChangeHandlers<Date>, T> extends AbstractHandlerAdapter<M, T, ValueChangeHandler<Date>> {

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