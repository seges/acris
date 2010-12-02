package sk.seges.acris.security.client.handler;

import com.google.gwt.event.shared.HasHandlers;

public interface HasOpenIDLoginHandlers extends HasHandlers {
	
	void addOpenIDLoginHandler(OpenIDLoginHandler handler);
}
