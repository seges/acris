package sk.seges.acris.security.shared.user_management.domain;

import sk.seges.acris.security.shared.user_management.domain.api.LocaleLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;

public class OpenIDLoginToken implements LocaleLoginToken {

	private static final long serialVersionUID = -1409051773288396870L;

	private String identifier;
	private String email;
	private OpenIDProvider provider;
	private String webId;
	private String locale;
	private boolean admin;
	
	public OpenIDLoginToken() {
		this(null, null, null, null, false);
	}

	public OpenIDLoginToken(String identifier, String email, OpenIDProvider provider, String webId, boolean admin) {
		this(identifier, email, provider, webId, null, admin);
	}

	public OpenIDLoginToken(String identifier, String email, OpenIDProvider provider, String webId, String locale, boolean admin) {
		this.identifier = identifier;
		this.email = email;
		this.provider = provider;
		this.webId = webId;
		this.locale = locale;
		this.admin = admin;
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
	
	@Override
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	@Override
	public boolean isAdmin() {
		return admin;
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
