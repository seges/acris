package sk.seges.acris.security.server.util;

import sk.seges.acris.security.shared.core.user_management.domain.hibernate.HibernateSecurityRole;

public class LoggedUserRole extends HibernateSecurityRole {
	private static final long serialVersionUID = -8975142282892294530L;
	
	public LoggedUserRole() {
		setName(LoginConstants.LOGGED_ROLE_NAME);
	}
}
