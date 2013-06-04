package sk.seges.acris.security.rpc.user_management.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import sk.seges.acris.security.rpc.authority.GrantedAuthorityImpl;
import sk.seges.sesam.domain.IDomainObject;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "generic_users")
public class GenericUser implements IDomainObject<Long>, UserDetails {

	private static final long serialVersionUID = 4295318098990134331L;

	public static final String USER_NAME_ATTRIBUTE = "username";
	public static final String PASSWORD_ATTRIBUTE = "password";
	public static final String ENABLED_ATTRIBUTE = "enabled";
	public static final String DESCRIPTION_ATTRIBUTE = "description";

	public static final String ROLE_PREFIX = "ROLE_";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String description;

	@Column
	private boolean enabled;

	@Column
	private String username;

	@Column
	private String password;

	@Transient
	protected List<String> authorities;

	@OneToOne(cascade = CascadeType.ALL)
	private UserPreferences userPreferences;

	public GenericUser() {}

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

	public boolean hasAuthority(String authority) {

		if (authorities != null) {
			return authorities.contains(authority) || authorities.contains(ROLE_PREFIX + authority);
		}

		return false;
	}

	public List<String> getUserAuthorities() {
		return authorities;
	}

	public void setUserAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public GrantedAuthority[] getAuthorities() {
		if (authorities == null) {
			return new GrantedAuthority[0];
		}

		GrantedAuthority[] grantedAuthorities = new GrantedAuthority[authorities.size()];

		int i = 0;
		for (String authority : authorities) {
			GrantedAuthorityImpl lazyGrantedAuthority = new GrantedAuthorityImpl();
			lazyGrantedAuthority.setAuthority(authority);
			grantedAuthorities[i++] = lazyGrantedAuthority;
		}

		return grantedAuthorities;
	}

	public void setAuthorities(GrantedAuthority[] authorities) {
		this.authorities = new ArrayList<String>();

		if (authorities != null) {
			for (GrantedAuthority authority : authorities) {
				this.authorities.add(authority.getAuthority());
			}
		}
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericUser other = (GenericUser) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GenericUser [authorities=" + authorities + ", description=" + description + ", enabled="
				+ enabled + ", id=" + id + ", password=" + password + ", userPreferences=" + userPreferences
				+ ", username=" + username + "]";
	}
}