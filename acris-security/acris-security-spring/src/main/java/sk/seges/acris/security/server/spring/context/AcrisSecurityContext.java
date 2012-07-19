package sk.seges.acris.security.server.spring.context;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextImpl;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;

import sk.seges.acris.security.shared.spring.authority.GrantedAuthorityImpl;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.dto.SecurityRoleDTO;

/**
 * Acris Security Context 
 * By default return User with '*' user name as authentication if
 * no authentication is set to security context
 * 
 * @author psloboda
 */
public class AcrisSecurityContext extends SecurityContextImpl {
	
	private static final long serialVersionUID = -3151911186106927140L;
	private AnonymousAuthenticationToken authentication;
	
	@Override
	public Authentication getAuthentication() {
		if (super.getAuthentication() == null) {
			if (authentication == null) {
				GrantedAuthorityImpl authority = new GrantedAuthorityImpl();
				authority.setAuthority(Permission.VIEW.name());
				
				User u = new User(SecurityRoleDTO.ALL_USERS, "", true, true, true, true, new GrantedAuthorityImpl[]{authority});
				authentication = new AnonymousAuthenticationToken(u.getUsername(), u, u.getAuthorities());
			}
			return authentication;
		}
		return super.getAuthentication();
	}
}
