package sk.seges.acris.security.shared.core.user_management.domain.hibernate;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import sk.seges.acris.security.server.core.user_management.WebIDUserRole;
import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaUserPreferences;
import sk.seges.corpis.server.domain.user.UserRole;
import sk.seges.corpis.server.domain.user.server.model.base.UserBase;
import sk.seges.corpis.server.domain.user.server.model.data.UserPreferencesData;
import sk.seges.corpis.server.domain.user.server.model.data.UserRoleData;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "generic_users")
public class HibernateGenericUser extends UserBase {

	private static final long serialVersionUID = -4335463902019393342L;

	public HibernateGenericUser() {	
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
	
	/*@CollectionOfElements
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "generic_users_userauthorities", joinColumns = @JoinColumn(name = "generic_users_id"))*/
	@Transient
	public List<String> getUserAuthorities() {
		return super.getUserAuthorities();
	}

	@Column
	public boolean isEnabled() {
		return super.isEnabled();
	}

	@NotNull
	@Column(nullable = false)
	public String getUsername() {
		return super.getUsername();
	}

	@Column
	public String getPassword() {
		return super.getPassword();
	}
	
	@Column
	public String getContact() {
		return super.getContact();
	}
	
	@Column
	public String getEmail() {
		return super.getEmail();
	}
	
	@Column
	public String getName() {
		return super.getName();
	}

	@Column
	public String getSurname() {
		return super.getSurname();
	}
	
	@Column
	public String getWebId() {
		return super.getWebId();
	}
	
	@OneToOne(cascade = CascadeType.ALL, targetEntity = JpaUserPreferences.class)
	public UserPreferencesData getUserPreferences() {
		return super.getUserPreferences();
	}
	
	@OneToMany(mappedBy=WebIDUserRole.USER, targetEntity=WebIDUserRole.class)
	public List<UserRoleData> getUserRoles(){
		return super.getUserRoles();
	}
}