package sk.seges.acris.security.shared.user_management.domain;

import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

/**
 * @author psloboda
 */
public class AnonymousLoginToken implements LoginToken {
	private static final long serialVersionUID = -8817688681729146396L;
	
	private String webId;
	private Object principal;
	
	public AnonymousLoginToken() {}
	
	public AnonymousLoginToken(Object principal, String webId) {
		this.principal = principal;
		this.webId = webId;
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
}