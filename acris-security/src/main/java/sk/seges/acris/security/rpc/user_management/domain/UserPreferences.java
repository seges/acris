package sk.seges.acris.security.rpc.user_management.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import sk.seges.sesam.domain.IDomainObject;

@Table
@Entity
public class UserPreferences implements IDomainObject<Long> {

	private static final long serialVersionUID = -2671953566571056404L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String locale;

	public UserPreferences() {	
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
