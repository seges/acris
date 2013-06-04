package sk.seges.acris.security.shared.core.user_management.domain.hibernate.gilead;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.gilead.pojo.base.ILightEntity;
import net.sf.gilead.pojo.gwt.IGwtSerializableParameter;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import sk.seges.acris.security.shared.core.user_management.domain.jpa.JpaUserPreferences;
import sk.seges.acris.security.shared.user_management.domain.api.UserPreferences;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "generic_users")
public class GileadHibernateGenericUser extends GenericUserDTO implements ILightEntity {

	private static final long serialVersionUID = -5241287338707370315L;

	public GileadHibernateGenericUser() {
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

	@CollectionOfElements
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "generic_users_userauthorities", joinColumns = @JoinColumn(name = "generic_users_id"))
	public List<String> getUserAuthorities() {
		return authorities;
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

	@OneToOne(cascade = CascadeType.ALL, targetEntity = JpaUserPreferences.class)
	public UserPreferences getUserPreferences() {
		return super.getUserPreferences();
	}

	protected Map<String, IGwtSerializableParameter> _proxyInformations;

	protected Map<String, Boolean> _initializationMap;

	public void addProxyInformation(String property, Object proxyInfo) {
		if (_proxyInformations == null) {
			_proxyInformations = new HashMap<String, IGwtSerializableParameter>();
		}
		_proxyInformations.put(property, (IGwtSerializableParameter) proxyInfo);
	}

	public void removeProxyInformation(String property) {
		if (_proxyInformations != null) {
			_proxyInformations.remove(property);
		}
	}

	@Transient
	public Object getProxyInformation(String property) {
		if (_proxyInformations != null) {
			return _proxyInformations.get(property);
		} else {
			return null;
		}
	}

	@Transient
	public String getDebugString() {
		if (_proxyInformations != null) {
			return _proxyInformations.toString();
		} else {
			return null;
		}
	}

	@Transient
	public boolean isInitialized(String property) {
		if (_initializationMap == null) {
			return true;
		}

		Boolean initialized = _initializationMap.get(property);
		if (initialized == null) {
			return true;
		}
		return initialized.booleanValue();
	}

	public void setInitialized(String property, boolean initialized) {
		if (_initializationMap == null) {
			_initializationMap = new HashMap<String, Boolean>();
		}
		_initializationMap.put(property, initialized);
	}

	@Transient
	public Object getUnderlyingValue() {
		return this;
	}
}