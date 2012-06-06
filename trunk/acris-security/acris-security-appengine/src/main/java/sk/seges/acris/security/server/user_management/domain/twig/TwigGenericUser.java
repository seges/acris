package sk.seges.acris.security.server.user_management.domain.twig;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.SecurityConstants;
import sk.seges.acris.security.shared.user_management.domain.api.RoleData;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserPreferences;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;

import com.vercer.engine.persist.annotation.Embed;
import com.vercer.engine.persist.annotation.Key;
import com.vercer.engine.persist.annotation.Store;

public class TwigGenericUser implements UserData<Long> {

	private static final long serialVersionUID = 8007659656583555159L;

	@Key
	private Long id;

	private String description;

	private boolean enabled;

	private String username;

	private String password;

	protected @Embed List<TwigString> authorities;

	private @Store(false)
	TwigUserPreferences userPreferences;

	private String webId;
	
	private List<RoleData> roles;
	
	public TwigGenericUser() {
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
		this.userPreferences = (TwigUserPreferences) userPreferences;
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

	public boolean hasAuthority(String authority) {

		if (authorities != null) {
			return authorities.contains(authority) || authorities.contains(SecurityConstants.AUTH_PREFIX + authority);
		}

		return false;
	}

	public List<String> getUserAuthorities() {
		if (authorities == null) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		for (TwigString twigString : authorities) {
			result.add(twigString.getValue());
		}
		return result;
	}

	public void setUserAuthorities(List<String> authorities) {
		if (authorities == null) {
			this.authorities = null;
			return;
		}

		List<TwigString> result = new ArrayList<TwigString>();

		long i = 1;
		for (String authority : authorities) {
			TwigString twigString = new TwigString();
			twigString.setId(i);
			twigString.setValue(authority);
			i++;
			result.add(twigString);
		}
		this.authorities = result;
	}
	
	@Override
	public String getWebId() {
		return webId;
	}

	@Override
	public void setWebId(String webId) {
		this.webId = webId;
	}

	@Override
	public List<RoleData> getRoles() {
		return roles;
	}

	@Override
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
			if (other.getUsername() != null) return false;
		} else if (!username.equals(other.getUsername())) return false;
		return true;
	}

	@Override
	public String toString() {
		return "TwigGenericUser [authorities=" + authorities + ", description=" + description + ", enabled=" + enabled + ", id=" + id + ", password="
				+ password + ", userPreferences=" + userPreferences + ", username=" + username + ", webId=" + webId + "]";
	}
}