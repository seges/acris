package sk.seges.acris.security.shared.user_management.domain.dto;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.RoleData;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserPreferences;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
public class GenericUserDTO implements UserData {

	private static final long serialVersionUID = 6311173194141382224L;

	private Long id;

	private String description;

	private boolean enabled;

	private String username;

	private String password;

	protected List<String> authorities;

	private UserPreferences userPreferences;
	
	private String webId;
	
	private List<RoleData> roles;
	
	private String name;
	
	private String surname;
	
	private String contact;
	
	private String email;
	
	public GenericUserDTO() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isAccountNonExpired() {
		return enabled;
	}

	public boolean isAccountNonLocked() {
		return enabled;
	}

	public boolean isCredentialsNonExpired() {
		return enabled;
	}

	public List<String> getUserAuthorities() {
		return authorities;
	}

	public void setUserAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public String getWebId() {
		return webId;
	}
	
	public void setWebId(String webId) {
		this.webId = webId;
	}
	
	public List<RoleData> getRoles() {
		return roles;
	}
	
	public void setRoles(List<RoleData> roles) {
		this.roles = roles;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GenericUserDTO other = (GenericUserDTO) obj;
		if (username == null) {
			if (other.username != null) return false;
		} else if (!username.equals(other.username)) {
			return false;
		} else if (webId == null) {
			if (other.webId != null) return false;
		} else if (!webId.equals(other.webId))  return false;
		return true;
	}

	@Override
	public String toString() {
		return "GenericUser [authorities=" + authorities + ", description=" + description + ", enabled=" + enabled + ", id=" + id + ", password=" + password
				+ ", userPreferences=" + userPreferences + ", username=" + username + ", webId=" + webId + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}