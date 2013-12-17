package sk.seges.acris.security.server.core.user_management.domain.jpa;

import sk.seges.corpis.server.domain.user.server.model.base.UserBase;
import sk.seges.corpis.server.domain.user.server.model.data.UserPreferencesData;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "generic_users")
public class JpaGenericUser extends UserBase {

	private static final long serialVersionUID = -4335463902019393342L;

	public JpaGenericUser() {	
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return super.getId();
	}

	@Column
	public String getDescription() {
		return super.getDescription();
	}

	@Column
	public boolean isEnabled() {
		return super.isEnabled();
	}

	@Column
	public String getUsername() {
		return super.getUsername();
	}

	@Column
	public String getPassword() {
		return super.getPassword();
	}

	@OneToMany(fetch=FetchType.EAGER)
	public List<JpaStringEntity> getAuthorities() {
		List<JpaStringEntity> authorities = new ArrayList<JpaStringEntity>();
		for (String authority: super.getUserAuthorities()) {
			JpaStringEntity entity = new JpaStringEntity();
			entity.setId(getId());
			entity.setValue(authority);
			authorities.add(entity);
		}
		return authorities;
	}

	public void setAuthorities(List<JpaStringEntity> authorities) {
		List<String> result = new ArrayList<String>();
		for (JpaStringEntity authority: authorities) {
			result.add(authority.getValue());
		}
		setUserAuthorities(result);
	}

	@OneToOne(cascade = CascadeType.ALL, targetEntity = JpaUserPreferences.class)
	public UserPreferencesData getUserPreferences() {
		return super.getUserPreferences();
	}
}