package sk.seges.acris.security.shared.user_management.domain.api;

import java.io.Serializable;
import java.util.List;

public interface UserRolePermission extends Serializable {

	String getPermission();

	void setPermission(String permission);

	List<String> getUserPermissions();

}
