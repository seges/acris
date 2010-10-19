package sk.seges.acris.security.shared.core.user_management.domain.hibernate.gilead;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.gilead.pojo.base.ILightEntity;
import net.sf.gilead.pojo.gwt.IGwtSerializableParameter;
import sk.seges.acris.security.shared.core.user_management.domain.hibernate.HibernateRolePermission;

@Entity
@Table(name="rolepermission")
public class GileadHibernateRolePermission extends HibernateRolePermission implements ILightEntity {

	private static final long serialVersionUID = -5827961597276260668L;

	public GileadHibernateRolePermission() {	
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