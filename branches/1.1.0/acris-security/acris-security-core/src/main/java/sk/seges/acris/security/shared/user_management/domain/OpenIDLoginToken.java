package sk.seges.acris.security.shared.user_management.domain;

import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

public class OpenIDLoginToken implements LoginToken {

	private static final long serialVersionUID = -1409051773288396870L;

	private String identifier;

	public OpenIDLoginToken() {
	}

	public OpenIDLoginToken(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
