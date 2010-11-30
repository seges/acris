package sk.seges.acris.mvp.shared.result.user;

import sk.seges.acris.security.shared.session.ClientSession;

import com.gwtplatform.dispatch.shared.Result;

public class LoginResult implements Result {

	private static final long serialVersionUID = -1231048485510366736L;

	/**
	 * For serialization only.
	 */
	@SuppressWarnings("unused")
	private LoginResult() {
	}

	private ClientSession clientSession;

	public LoginResult(ClientSession clientSession) {
		this.clientSession = clientSession;
	}

	public ClientSession getClientSession() {
		return clientSession;
	}
}