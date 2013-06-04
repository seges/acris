package sk.seges.acris.security.client.handler;

import sk.seges.acris.security.client.event.OpenIDLoginEvent;

import com.google.gwt.event.shared.EventHandler;

public interface OpenIDLoginHandler extends EventHandler {

	public void onSubmit(OpenIDLoginEvent openIDLoginEvent);
}
