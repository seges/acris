package sk.seges.acris.security.server.spring.user_management.domain;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class SpringUserAdapter implements UserDetails {

	private static final long serialVersionUID = 5904509684815616154L;

	private SpringAuthoritiesSupport springSupport;
	
	private UserData userData;
	
	public SpringUserAdapter(UserData userData) {
		springSupport = new SpringAuthoritiesSupport(userData);
	}
	
	public GrantedAuthority[] getAuthorities() {
		return springSupport.getAuthorities();
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		springSupport.setAuthorities(authorities);
	}

	@Override
	public String getPassword() {
		return userData.getPassword();
	}

	@Override
	public String getUsername() {
		return userData.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return userData.isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return userData.isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return userData.isEnabled();
	}

	@Override
	public boolean isEnabled() {
		return userData.isEnabled();
	}
}