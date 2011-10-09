package sk.seges.acris.security.shared.core.user_management.domain.jpa.gilead;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.gilead.pojo.base.ILightEntity;
import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermission;
import sk.seges.acris.security.shared.user_management.domain.dto.SecurityPermissionDTO;

@Entity
@Table(name = "permission")
@SequenceGenerator(name = "permission_id_seq", sequenceName = "permission_id_seq", initialValue = 1)
public class GileadJpaSecurityPermission extends SecurityPermissionDTO implements ILightEntity {

	private static final long serialVersionUID = -5827961597276260668L;

	public GileadJpaSecurityPermission() {
	}
	
	protected Map<String, String> _proxyInformations;

    @Override
    @ManyToOne(targetEntity=GileadJpaSecurityPermission.class)
    public HierarchyPermission getParent() {
    	return super.getParent();
    }
    
    @OneToMany(mappedBy="parent", targetEntity=GileadJpaSecurityPermission.class, cascade=CascadeType.ALL)
    @Override
    public List<HierarchyPermission> getChildPermissions() {
    	return super.getChildPermissions();
    }
    
	@Id
    @GeneratedValue(generator="permission_id_seq")
    @Override
    public Integer getId() {
    	return super.getId();
    }
    
    @Column
    @Override
    public String getDiscriminator() {
    	return super.getDiscriminator();
    }
    
    @Column(nullable=false)
    @Override
    public String getWebId() {
    	return super.getWebId();
    }

    @Column
    @Override
    public Boolean getHasChildren() {
    	return super.getHasChildren();
    }

    @Column
    @Override
    public Integer getLevel() {
    	return super.getLevel();
    }
    
    @Column
    @Override
	public String getPermission() {
		return super.getPermission();
	}
	
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

	@Transient
	public String getDebugString() {
		if (_proxyInformations != null) {
			return _proxyInformations.toString();
		} else {
			return null;
		}
	}
}
