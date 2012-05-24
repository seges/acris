/**
 * 
 */
package sk.seges.acris.binding.client.providers.support.handlers;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;
import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider.AbstractHandlerAdapter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class ClickHandlerAdapter<M extends HasClickHandlers, T> extends AbstractHandlerAdapter<M, T, ClickHandler> {
	
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