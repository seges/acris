package sk.seges.acris.security.shared.user_management.domain.dto;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.UserRolePermission;
import sk.seges.sesam.domain.IDomainObject;

public class RolePermissionDTO implements IDomainObject<String>, UserRolePermission {

    private static final long serialVersionUID = -8726176377885701281L;

	protected String permission;

	protected List<String> userPermissions;

	public RolePermissionDTO() {	
	}
	
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public List<String> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(List<String> userPermissions) {
		this.userPermissions = userPermissions;
	}

	@Override
	public String getId() {
		return permission;
	}
}