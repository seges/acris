package sk.seges.acris.security.server.spring.user_management.domain;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;

public class SpringGenericUser extends GenericUserDTO implements UserDetails {

	private static final long serialVersionUID = -8651786622812062383L;

	private SpringAuthoritiesSupport springSupport = new SpringAuthoritiesSupport(this);
	
	public GrantedAuthority[] getAuthorities() {
		return springSupport.getAuthorities();
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		springSupport.setAuthorities(authorities);
	}
}