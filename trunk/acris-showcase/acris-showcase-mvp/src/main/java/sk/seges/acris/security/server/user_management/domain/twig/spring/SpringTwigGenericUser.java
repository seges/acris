package sk.seges.acris.security.server.user_management.domain.twig.spring;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import sk.seges.acris.security.server.spring.user_management.domain.SpringAuthoritiesSupport;
import sk.seges.acris.security.server.user_management.domain.twig.TwigGenericUser;

import com.vercer.engine.persist.annotation.Store;


public class SpringTwigGenericUser extends TwigGenericUser implements UserDetails {

	private static final long serialVersionUID = 5377487255515011977L;

	private @Store(false) SpringAuthoritiesSupport springSupport = new SpringAuthoritiesSupport(this);

	public GrantedAuthority[] getAuthorities() {
		return springSupport.getAuthorities();
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		springSupport.setAuthorities(authorities);
	}
}