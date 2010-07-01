package sk.seges.acris.security.rpc.user_management.domain;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * {@link RolePermission} and {@link IUserPermission} holder used for harvesting
 * authorities to the user. Mostly this holder is represented by Role to which 
 * can user be assigned. Role has assigned {@link RolePermission} and 
 * {@link IUserPermission} and they are propagated to the user as GrantedAuthories.
 * </pre>
 * 
 * @author fat
 * 
 * @param <T>
 *            Concrete implementation of the {@link IUserPermission}.
 */
public interface IGroupAuthoritiesHolder<T extends IUserPermission> extends
		Serializable {

	public List<RolePermission> getRolePermissions();

	public void setRolePermissions(List<RolePermission> rolePermissions);

	public List<T> getUserPermissions();

	public void setUserPermissions(List<T> userPermissions);
}
