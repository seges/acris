package sk.seges.acris.mvp.shared.action.user;

import sk.seges.acris.mvp.shared.action.core.AbstractAction;
import sk.seges.acris.mvp.shared.result.user.LoginResult;

public class LoginAction extends AbstractAction<LoginResult> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private LoginAction() {
	}

	private String username;

	private String password;

	public LoginAction(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}
}