package sk.seges.acris.security.shared.user_management.domain.dto;

import sk.seges.acris.security.shared.user_management.domain.api.HasOpenIDIdentifier;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class OpenIDUserDTO implements HasOpenIDIdentifier {

	private static final long serialVersionUID = 1961068463062486129L;

	private UserData<?> user;

	private String identifier;

	@Override
	public UserData<?> getUser() {
		return user;
	}

	@Override
	public void setUser(UserData<?> user) {
		this.user = user;
	}

	@Override
	public String getId() {
		return identifier;
	}

	@Override
	public void setId(String identifier) {
		this.identifier = identifier;
	}
}
