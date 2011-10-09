package sk.seges.acris.security.shared.core.user_management.domain.hibernate.gilead;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.gilead.pojo.base.ILightEntity;
import sk.seges.acris.security.shared.core.user_management.domain.hibernate.HibernateRolePermission;

@Entity
@Table(name="rolepermission")
public class GileadHibernateRolePermission extends HibernateRolePermission implements ILightEntity {

	private static final long serialVersionUID = -5827961597276260668L;

	public GileadHibernateRolePermission() {	
	}
	
	protected Map<String, String> _proxyInformations;

	@Transient
	public Map<String, String> getProxyInformations() {
		return _proxyInformations;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public void setProxyInformations(Map informations) {
		_proxyInformations = informations;
	}

	public void addProxyInformation(String property, String proxyInfo) {
		if (_proxyInformations == null) {
			_proxyInformations = new HashMap<String, String>();
		}
		_proxyInformations.put(property, proxyInfo);
	}

	public void removeProxyInformation(String property) {
		if (_proxyInformations != null) {
			_proxyInformations.remove(property);
		}
	}

	@Transient
	public String getProxyInformation(String property) {
		if (_proxyInformations != null) {
			return _proxyInformations.get(property);
		} else {
			return null;
		}
	}

	public String getDebugString() {
		if (_proxyInformations != null) {
			return _proxyInformations.toString();
		} else {
			return null;
		}
	}
}