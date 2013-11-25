package sk.seges.acris.security.server.spring.user_management.service.provider;

import java.util.Collection;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class WebIdAnonymousAuthenticationToken extends AnonymousAuthenticationToken {
	private static final long serialVersionUID = 2411609719059986516L;

	private String webId;
	
	public WebIdAnonymousAuthenticationToken(String key, Object principal, String webId, Collection<? extends GrantedAuthority> authorities) {
		super(key, principal, authorities);
		this.webId = webId;
	}

	public String getWebId() {
		return webId;
	}
}