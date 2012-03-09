package sk.seges.acris.security.shared.user_management.domain;

import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;

public class OpenIDLoginToken implements LoginToken {

	private static final long serialVersionUID = -1409051773288396870L;

	private String identifier;
	private String email;
	private OpenIDProvider provider;
	private String webId;
	
	public OpenIDLoginToken() {
		this(null, null, null, null);
	}

	public OpenIDLoginToken(String identifier, String email, OpenIDProvider provider, String webId) {
		this.identifier = identifier;
		this.email = email;
		this.provider = provider;
		this.webId = webId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public OpenIDProvider getProvider() {
		return provider;
	}

	public void setProvider(OpenIDProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}
}
