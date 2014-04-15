package sk.seges.acris.security.server.spring.user_management.service.provider;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class HasWebIDAuthenticationToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = -2178612045597517021L;
	
	private String webId;
	
	public HasWebIDAuthenticationToken(Object principal, Object credentials, String webId) {
		super(principal, credentials);
		this.webId = webId;
	}
	
    public HasWebIDAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String webId) {
        super(principal, credentials, authorities);
        this.webId = webId;
    }
    
    public String getWebId() {
    	return webId;
    }
}

