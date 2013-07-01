package sk.seges.acris.security.server.user_management.domain.twig;

import sk.seges.corpis.server.domain.user.server.model.data.UserPreferencesData;
import sk.seges.sesam.domain.IMutableDomainObject;

import com.vercer.engine.persist.annotation.Key;

public class TwigUserPreferences implements IMutableDomainObject<Long>, UserPreferencesData {

	private static final long serialVersionUID = -2671953566571056404L;

	private @Key Long id;

	private String locale;

	public TwigUserPreferences() {	
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