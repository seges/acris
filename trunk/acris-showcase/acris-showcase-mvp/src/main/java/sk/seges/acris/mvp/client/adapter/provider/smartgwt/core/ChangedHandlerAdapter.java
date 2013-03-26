package sk.seges.acris.mvp.client.adapter.provider.smartgwt.core;

import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider;
import sk.seges.acris.binding.client.providers.support.generic.HandlerBindingAdapterProvider.AbstractHandlerAdapter;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.HasChangedHandlers;

public class ChangedHandlerAdapter<M extends HasChangedHandlers, T> extends AbstractHandlerAdapter<M, T, ChangedHandler> {

	public ChangedHandlerAdapter(M handlerWidget, String property, HandlerBindingAdapterProvider<M, T, ChangedHandler> provider) {
		super(handlerWidget, property, provider);
	}

	protected HandlerRegistration addHandlerToWidget(ChangedHandler handler) {
		return widget.addChangedHandler(handler);
	}

	protected ChangedHandler createHandler() {
		return new Handler();
	}

	private class Handler implements ChangedHandler {

		public void onChanged(ChangedEvent arg0) {
			Object oldElementOrElements = previousValue;
			previousValue = getValue();
			firePropertyChange(oldElementOrElements, previousValue);
		}
	}
}