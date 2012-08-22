package sk.seges.acris.security.shared.user_management.domain.dto;

import sk.seges.acris.security.shared.user_management.domain.api.UserPreferences;
import sk.seges.sesam.domain.IMutableDomainObject;

public class UserPreferencesDTO implements IMutableDomainObject<Long>, UserPreferences {

	private static final long serialVersionUID = -2671953566571056404L;

	private Long id;

	private String locale;

	public UserPreferencesDTO() {	
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}