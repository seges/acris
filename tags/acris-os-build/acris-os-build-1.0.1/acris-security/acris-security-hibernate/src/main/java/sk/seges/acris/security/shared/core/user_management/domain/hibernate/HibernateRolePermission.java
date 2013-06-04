package sk.seges.acris.security.shared.core.user_management.domain.hibernate;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import sk.seges.acris.security.shared.user_management.domain.dto.RolePermissionDTO;

@Entity
@Table(name="rolepermission")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISC", discriminatorType=DiscriminatorType.INTEGER)
public class HibernateRolePermission extends RolePermissionDTO {

    private static final long serialVersionUID = -8726176377885701281L;

    public HibernateRolePermission() {	
    }
    
    @Column
	@Id
	public String getPermission() {
    	return super.getPermission();
    }

	@CollectionOfElements(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
//	@JoinTable(name="rolepermisson_userpermissions", joinColumns=@JoinColumn(name="rolepermission_permission"))
	public List<String> getUserPermissions() {
		return super.getUserPermissions();
	}
}