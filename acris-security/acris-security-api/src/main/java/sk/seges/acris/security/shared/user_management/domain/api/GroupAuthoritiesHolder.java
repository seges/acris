package sk.seges.acris.security.shared.user_management.domain.api;

import java.io.Serializable;
import java.util.List;


/**
 * <pre>
 * {@link RolePermissionDTO} and {@link UserPermission} holder used for harvesting
 * authorities to the user. Mostly this holder is represented by Role to which 
 * can user be assigned. Role has assigned {@link RolePermissionDTO} and 
 * {@link UserPermission} and they are propagated to the user as GrantedAuthories.
 * </pre>
 * 
 * @author fat
 * 
 * @param <T>
 *            Concrete implementation of the {@link UserPermission}.
 */
public interface GroupAuthoritiesHolder<T extends UserPermission> extends
		Serializable {

	public List<T> getUserPermissions();

	public void setUserPermissions(List<T> userPermissions);
}
