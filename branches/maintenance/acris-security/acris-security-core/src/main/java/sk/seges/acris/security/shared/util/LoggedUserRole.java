package sk.seges.acris.security.shared.util;

import sk.seges.acris.security.shared.user_management.domain.dto.SecurityRoleDTO;

public class LoggedUserRole extends SecurityRoleDTO {
	private static final long serialVersionUID = -8975142282892294530L;

	public static final String LOGGED_ROLE_NAME = "logged_user_role";
	
	public LoggedUserRole() {
		setName(LOGGED_ROLE_NAME);
	}
}
