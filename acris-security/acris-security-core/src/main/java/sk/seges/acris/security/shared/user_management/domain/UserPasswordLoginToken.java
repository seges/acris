/**
 * 
 */
package sk.seges.acris.security.shared.user_management.domain;

import sk.seges.acris.security.shared.user_management.domain.api.LocaleLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

/**
 * Basic {@link LoginToken} implementation with username, password and language
 * information.
 * 
 * @author ladislav.gazo
 */
public class UserPasswordLoginToken implements LocaleLoginToken {
	
	private static final long serialVersionUID = 1338554746369372705L;

	private String username;
	private String password;
	private String language;
	private String webId;
	private String locale;
	private boolean admin;

	public UserPasswordLoginToken() {}

	public UserPasswordLoginToken(String username, String password, String webId, boolean admin) {
		super();
		this.username = username;
		this.password = password;
		this.webId = webId;
		this.admin = admin;
	}
	
	public UserPasswordLoginToken(String username, String password, String language, String webId, String locale, boolean admin) {
		super();
		this.username = username;
		this.password = password;
		this.language = language;
		this.webId = webId;
		this.locale = locale;
		this.admin = admin;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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
