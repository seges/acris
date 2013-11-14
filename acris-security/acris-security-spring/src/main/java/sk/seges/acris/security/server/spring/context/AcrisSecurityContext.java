package sk.seges.acris.security.server.spring.context;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;

import sk.seges.acris.security.shared.spring.authority.GrantedAuthorityImpl;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.user_management.server.model.data.RoleData;

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
				List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
				authorityList.add(authority);
				User u = new User(RoleData.ALL_USERS, "", true, true, true, true, authorityList);
				authentication = new AnonymousAuthenticationToken(u.getUsername(), u, u.getAuthorities());
			}
			return authentication;
		}
		return super.getAuthentication();
	}
}
