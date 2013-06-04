/**
 * 
 */
package sk.seges.acris.security.rpc.user_management.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.gilead.pojo.base.ILightEntity;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.GrantedAuthority;

/**
 * @author ladislav.gazo
 */
@Entity
@Table(name = "user_with_authorities")
public class UserWithAuthorities extends GenericUser implements ILightEntity {
	private static final long serialVersionUID = 6755045948801685615L;

	@CollectionOfElements(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<String> selectedAuthorities;
	
	public List<String> getSelectedAuthorities() {
		return selectedAuthorities;
	}

	public void setSelectedAuthorities(List<String> selectedAuthorities) {
		this.selectedAuthorities = selectedAuthorities;
	}
	
	private void fillAuthorities() {
		if(authorities == null) {
			authorities = selectedAuthorities;
		}
	}
	
	@Override
	public boolean hasAuthority(String authority) {
		fillAuthorities();
		return super.hasAuthority(authority);
	}
	
	@Override
	public List<String> getUserAuthorities() {
		fillAuthorities();
		return super.getUserAuthorities();
	}
	
	@Override
	public void setUserAuthorities(List<String> authorities) {
		setSelectedAuthorities(authorities);
		super.setUserAuthorities(authorities);
	}
	
	@Override
	public GrantedAuthority[] getAuthorities() {
		fillAuthorities();
		return super.getAuthorities();
	}
	
	@Override
	public void setAuthorities(GrantedAuthority[] authorities) {
		super.setAuthorities(authorities);
		setSelectedAuthorities(this.authorities);
	}
	
	// gilead
	@Transient
	protected Map<String, String> _proxyInformations;
	public Map<String, String> getProxyInformations() {
		return _proxyInformations;
	}
	@SuppressWarnings("unchecked")
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
