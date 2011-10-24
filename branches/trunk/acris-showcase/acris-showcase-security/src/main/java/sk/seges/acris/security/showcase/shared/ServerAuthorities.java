package sk.seges.acris.security.showcase.shared;

import sk.seges.acris.security.shared.user_management.domain.Permission;

/**
 * @author ladislav.gazo
 */
public interface ServerAuthorities {
	public static final String SECURITY_MANAGEMENT_VIEW = Grants.SECURITY_MANAGEMENT + Permission.VIEW_SUFFIX;
}
