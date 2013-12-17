package sk.seges.acris.security.server.core.user_management.domain.jpa;

import sk.seges.corpis.server.domain.user.server.model.base.UserPreferencesBase;

import javax.persistence.*;

@Entity
@Table(name = "userpreferences")
public class JpaUserPreferences extends UserPreferencesBase {

	private static final long serialVersionUID = -2671953566571056404L;

	public JpaUserPreferences() {	
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return super.getId();
	}

	@Column
	public String getLocale() {
		return super.getLocale();
	}
}