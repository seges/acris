package sk.seges.acris.security.server.permission;

import org.springframework.security.acls.Permission;
import org.springframework.security.acls.domain.BasePermission;

public class ExtendedPermission extends BasePermission {
	
	private static final long serialVersionUID = 3552609610967596790L;
	
	public static final Permission SEARCH = new ExtendedPermission(1 << 5, 'S'); // 32
	
	static {
		registerPermissionsFor(ExtendedPermission.class);
	}

	private ExtendedPermission(int mask, char code) {
		super(mask, code);
	}
}