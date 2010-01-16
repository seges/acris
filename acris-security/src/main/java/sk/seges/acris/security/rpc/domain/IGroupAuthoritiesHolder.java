package sk.seges.acris.security.rpc.domain;

import java.io.Serializable;
import java.util.List;

public interface IGroupAuthoritiesHolder<T extends IUserPermission> extends Serializable {

	public List<RolePermission> getRolePermissions();

	public void setRolePermissions(List<RolePermission> rolePermissions);

	public List<T> getUserPermissions();

	public void setUserPermissions(List<T> userPermissions);
}
