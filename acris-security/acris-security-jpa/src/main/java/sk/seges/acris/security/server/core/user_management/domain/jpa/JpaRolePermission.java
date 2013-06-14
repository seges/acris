package sk.seges.acris.security.server.core.user_management.domain.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import sk.seges.acris.security.user_management.server.model.base.UserRolePermissionBase;

@Entity
@Table(name="rolepermission")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISC", discriminatorType=DiscriminatorType.INTEGER)
public class JpaRolePermission extends UserRolePermissionBase {

    private static final long serialVersionUID = -8726176377885701281L;

    public JpaRolePermission() {	
    }
    
    @Column
	@Id
	public String getPermission() {
    	return super.getPermission();
    }
    
	@OneToMany(fetch=FetchType.EAGER)
	public List<JpaStringEntity> getPermissions() {
		List<JpaStringEntity> authorities = new ArrayList<JpaStringEntity>();
		for (String authority: super.getUserPermissions()) {
			JpaStringEntity entity = new JpaStringEntity();
			entity.setId(new Integer(getId().hashCode()).longValue());
			entity.setValue(authority);
			authorities.add(entity);
		}
		return authorities;
	}
		
	public void setAuthorities(List<JpaStringEntity> authorities) {
		List<String> result = new ArrayList<String>();
		for (JpaStringEntity authority: authorities) {
			result.add(authority.getValue());
		}
		super.setUserPermissions(result);
	}

	@Override
	public String getId() {
		return getPermission();
	}
}