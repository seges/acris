package sk.seges.acris.security.client.handler;

import sk.seges.acris.security.client.event.LoginEvent;

import com.google.gwt.event.shared.EventHandler;

public interface LoginHandler extends EventHandler {

	public void onSubmit(LoginEvent loginEvent);
}
