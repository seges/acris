package sk.seges.acris.security.shared.user_management.context;

import sk.seges.acris.security.shared.user_management.domain.api.UserContext;

/**
 * @author psloboda
 */
public class CommonUserContext implements UserContext {
	private static final long serialVersionUID = -840636618349258151L;
	
	private String webId;
	private String locale;
	
	public CommonUserContext() {}
	
	public CommonUserContext(String webId, String locale) {
		this.webId = webId;
		this.locale = locale;
	}
	
	@Override
	public String getWebId() {
		return webId;
	}
	
	@Override
	public String getLocale() {
		return locale;
	}
	
	public void setWebId(String webId) {
		this.webId = webId;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}
}
