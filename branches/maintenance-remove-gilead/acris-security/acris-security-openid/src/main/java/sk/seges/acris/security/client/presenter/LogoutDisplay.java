package sk.seges.acris.security.client.presenter;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface LogoutDisplay extends BaseDisplay {

	HandlerRegistration addLogoutButtonHandler(ClickHandler handler);
	
	void setLoggedUser(String userName);
}
