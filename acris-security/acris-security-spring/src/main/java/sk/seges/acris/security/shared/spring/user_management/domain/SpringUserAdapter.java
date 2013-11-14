package sk.seges.acris.security.shared.spring.user_management.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import sk.seges.acris.security.user_management.server.model.data.RoleData;
import sk.seges.acris.security.user_management.server.model.data.UserData;
import sk.seges.acris.security.user_management.server.model.data.UserPreferencesData;

public class SpringUserAdapter implements UserDetails, UserData {

	private static final long serialVersionUID = 5904509684815616154L;

	private SpringAuthoritiesSupport springSupport;
	
	public SpringUserAdapter(UserData userData) {
		assert userData != null;
		springSupport = new SpringAuthoritiesSupport(userData);
	}
	
	public UserData getUser() {
		return springSupport.getUser();
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return springSupport.getAuthorities();
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
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
	public void setId(Long id) {
		springSupport.getUser().setId(id);
	}

	@Override
	public Long getId() {
		return springSupport.getUser().getId();
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

	@Override
	public String getName() {
		return springSupport.getUser().getName();
	}

	@Override
	public void setName(String name) {
		springSupport.getUser().setName(name);
	}

	@Override
	public String getContact() {
		return springSupport.getUser().getContact();
	}

	@Override
	public void setContact(String contact) {
		springSupport.getUser().setContact(contact);
	}

	@Override
	public String getEmail() {
		return springSupport.getUser().getEmail();
	}

	@Override
	public void setEmail(String email) {
		springSupport.getUser().setEmail(email);
	}

	@Override
	public String getSurname() {
		return springSupport.getUser().getSurname();
	}

	@Override
	public void setSurname(String surname) {
		springSupport.getUser().setSurname(surname);
	}

	@Override
	public String getDescription() {
		return springSupport.getUser().getDescription();
	}

	@Override
	public void setDescription(String description) {
		springSupport.getUser().setDescription(description);
	}

	@Override
	public UserPreferencesData getUserPreferences() {
		return springSupport.getUser().getUserPreferences();
	}

	@Override
	public void setUserPreferences(UserPreferencesData userPreferences) {
		springSupport.getUser().setUserPreferences(userPreferences);
	}
}