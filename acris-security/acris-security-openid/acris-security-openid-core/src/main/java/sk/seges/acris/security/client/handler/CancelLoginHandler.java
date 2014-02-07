package sk.seges.acris.security.client.handler;

import sk.seges.acris.security.client.event.CancelLoginEvent;

import com.google.gwt.event.shared.EventHandler;

public interface CancelLoginHandler extends EventHandler {

	public void onSubmit(CancelLoginEvent loginEvent);
}
