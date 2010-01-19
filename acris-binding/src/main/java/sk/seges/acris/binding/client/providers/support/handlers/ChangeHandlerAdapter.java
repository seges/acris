/**
 * 
 */
package sk.seges.acris.binding.client.providers.support.handlers;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;
import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider.AbstractHandlerAdapter;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class ChangeHandlerAdapter<M extends HasChangeHandlers, T> extends AbstractHandlerAdapter<M, T, ChangeHandler> {

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