package sk.seges.acris.security.client.event;

import sk.seges.acris.common.util.Pair;
import sk.seges.acris.security.client.handler.LoginHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author ladislav.gazo
 */
public class LoginEvent extends GwtEvent<LoginHandler> {

	private static Type<LoginHandler> TYPE = new Type<LoginHandler>();

	private String username;
	private String password;
	private Pair<String, String> language;

	public LoginEvent() {
	}

	public LoginEvent(String username, String password, Pair<String, String> language) {
		super();
		this.username = username;
		this.password = password;
		this.language = language;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Pair<String, String> getLanguage() {
		return language;
	}

	public void setLanguage(Pair<String, String> language) {
		this.language = language;
	}

	public static Type<LoginHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<LoginHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginHandler handler) {
		handler.onSubmit(this);
	}
}
