package sk.seges.acris.security.shared.user_management.domain;

import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

/**
 * @author psloboda
 */
public class AnonymousLoginToken implements LoginToken {
	private static final long serialVersionUID = -8817688681729146396L;
	
	private String webId;
	private Object principal;
	private boolean admin;
	
	public AnonymousLoginToken() {}
	
	public AnonymousLoginToken(Object principal, String webId, boolean admin) {
		this.principal = principal;
		this.webId = webId;
		this.admin = admin;
	}
	
	@Override
	public String getWebId() {
		return webId;
	}
	
	public void setWebId(String webId) {
		this.webId = webId;
	}
	
	public Object getPrincipal() {
		return principal;
	}
	
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}
	
	@Override
	public boolean isAdmin() {
		return admin;
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}