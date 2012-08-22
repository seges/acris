package sk.seges.acris.security.shared.user_management.domain.dto;

import sk.seges.acris.security.shared.user_management.domain.api.HasOpenIDIdentifier;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class OpenIDUserDTO implements HasOpenIDIdentifier {

	private static final long serialVersionUID = 1961068463062486129L;

	private UserData user;

	private String identifier;

	private String email;

	private OpenIDProvider provider;

	@Override
	public UserData getUser() {
		return user;
	}

	@Override
	public void setUser(UserData user) {
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

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public OpenIDProvider getProvider() {
		return provider;
	}

	@Override
	public void setProvider(OpenIDProvider provider) {
		this.provider = provider;
	}
}
