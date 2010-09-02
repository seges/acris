package sk.seges.acris.mvp.client.event;

import sk.seges.acris.mvp.client.event.LoginEvent.LoginEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class LoginEvent extends GwtEvent<LoginEventHandler> {

	public static interface LoginEventHandler extends EventHandler {

		void onLogin(LoginEvent event);
	}

	private static final Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();

	public static Type<LoginEventHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginEventHandler handler) {
		handler.onLogin(this);
	}

	@Override
	public Type<LoginEventHandler> getAssociatedType() {
		return getType();
	}

	private String username;

	private String password;

	public LoginEvent(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}