package sk.seges.acris.mvp.client.event;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.mvp.client.event.LoginEvent.LoginEventHandler;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

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

	private UserData user;

	public LoginEvent(UserData user) {
		this.user = user;
	}

	public UserData getUser() {
		return user;
	}
}