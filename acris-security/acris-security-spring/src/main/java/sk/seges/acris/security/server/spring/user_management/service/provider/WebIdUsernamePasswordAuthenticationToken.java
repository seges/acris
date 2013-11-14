package sk.seges.acris.security.server.spring.user_management.service.provider;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class WebIdUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = 4187273929065295889L;
	
	private String webId;
	
    public WebIdUsernamePasswordAuthenticationToken(Object principal, Object credentials, String webId) {
        super(principal, credentials);
        this.webId = webId;
        setAuthenticated(false);
    }
    
    public WebIdUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String webId) {
        super(principal, credentials, authorities);
        this.webId = webId;
    }
    
    public String getWebId() {
    	return webId;
    }
}
