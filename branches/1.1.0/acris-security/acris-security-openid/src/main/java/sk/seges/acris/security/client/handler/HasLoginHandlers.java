package sk.seges.acris.security.client.handler;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasLoginHandlers extends HasHandlers {

	HandlerRegistration addLoginHandler(LoginHandler handler);
}
