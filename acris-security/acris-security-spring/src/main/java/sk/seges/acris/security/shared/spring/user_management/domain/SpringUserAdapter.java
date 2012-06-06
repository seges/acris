package sk.seges.acris.security.shared.spring.user_management.domain;

import java.util.List;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import sk.seges.acris.security.shared.user_management.domain.api.RoleData;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class SpringUserAdapter<E> implements UserDetails, UserData<E> {

	private static final long serialVersionUID = 5904509684815616154L;

	private SpringAuthoritiesSupport springSupport;
	
	public SpringUserAdapter(UserData<E> userData) {
		assert userData != null;
		springSupport = new SpringAuthoritiesSupport(userData);
	}
	
	public UserData<E> getUser() {
		return springSupport.getUser();
	}
	
	public GrantedAuthority[] getAuthorities() {
		return springSupport.getAuthorities();
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		springSupport.setAuthorities(authorities);
	}

	@Override
	public String getPassword() {
		return springSupport.getUser().getPassword();
	}

	@Override
	public String getUsername() {
		return springSupport.getUser().getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return springSupport.getUser().isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return springSupport.getUser().isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return springSupport.getUser().isEnabled();
	}

	@Override
	public boolean isEnabled() {
		return springSupport.getUser().isEnabled();
	}

	@Override
	public void setId(E t) {
		springSupport.getUser().setId(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E getId() {
		return (E) springSupport.getUser().getId();
	}

	@Override
	public boolean hasAuthority(String authority) {
		return springSupport.getUser().hasAuthority(authority);
	}

	@Override
	public List<String> getUserAuthorities() {
		return springSupport.getUser().getUserAuthorities();
	}

	@Override
	public void setUserAuthorities(List<String> authorities) {
		springSupport.getUser().setUserAuthorities(authorities);
	}

	@Override
	public void setUsername(String username) {
		springSupport.getUser().setUsername(username);
	}

	@Override
	public void setPassword(String password) {
		springSupport.getUser().setPassword(password);
	}

	@Override
	public void setEnabled(boolean enabled) {
		springSupport.getUser().setEnabled(enabled);
	}

	@Override
	public String getWebId() {
		return springSupport.getUser().getWebId();
	}

	@Override
	public void setWebId(String webId) {
		springSupport.getUser().setWebId(webId);
	}

	@Override
	public List<RoleData> getRoles() {
		return springSupport.getUser().getRoles();
	}

	@Override
	public void setRoles(List<RoleData> roles) {
		springSupport.getUser().setRoles(roles);
	}
}