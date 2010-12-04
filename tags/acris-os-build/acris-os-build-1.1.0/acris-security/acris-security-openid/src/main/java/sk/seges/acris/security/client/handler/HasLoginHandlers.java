package sk.seges.acris.security.client.handler;

import com.google.gwt.event.shared.HasHandlers;

public interface HasLoginHandlers extends HasHandlers {
	
	void addLoginHandler(LoginHandler handler);
}
