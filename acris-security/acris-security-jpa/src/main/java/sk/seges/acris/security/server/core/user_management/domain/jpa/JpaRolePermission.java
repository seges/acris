package sk.seges.acris.security.server.core.user_management.domain.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import sk.seges.acris.security.shared.user_management.domain.dto.RolePermissionDTO;

@Table
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISC", discriminatorType=DiscriminatorType.INTEGER)
public class JpaRolePermission extends RolePermissionDTO {

    private static final long serialVersionUID = -8726176377885701281L;

	@OneToMany(fetch=FetchType.EAGER)
	private Set<JpaStringEntity> permissions = new HashSet<JpaStringEntity>();

    @Column
	@Id
	public String getPermission() {
    	return super.getPermission();
    }

	public void setUserPermissions(List<String> permissions) {
		permissions.clear();
		for (String permission: permissions) {
			this.permissions.add(new JpaStringEntity(permission));
		}
		super.setUserPermissions(permissions);
	}
}