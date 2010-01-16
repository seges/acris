package sk.seges.acris.security.server.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.gilead.gwt.PersistentRemoteService;

import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import sk.seges.acris.security.rpc.domain.GenericUser;
import sk.seges.acris.security.rpc.service.IUserService;
import sk.seges.acris.security.rpc.to.ClientContext;

@Service
public class UserService extends PersistentRemoteService implements IUserService {

	@Autowired
	@Qualifier("loginUserService")
	private LoginUserService loginUserService;

//	@Autowired
//	private ISessionLogDAO sessionLogDao;

	private static final long serialVersionUID = 4347689255879212323L;

//	@Autowired
//	private IGenericUserDao genericUserDao;

//	@Autowired
//	public UserService(@Qualifier("acrisHibernateBeanManager") PersistentBeanManager lazyManager) {
//		super(lazyManager);
//	}

	public ClientContext login(String username, String password, String language/*, IUserDetail userDetail*/) {

		UserDetails user = loginUserService.loginUser(username, password, language);
		((GenericUser)user).setAuthorities(user.getAuthorities());

//		if (userDetail instanceof SessionLog) {
//			SessionLog sessionLog = (SessionLog)userDetail;
//			sessionLog.setUser((GenericUser)user);
//			sessionLogDao.add(sessionLog);
//		}

		HttpServletRequest request = ServletUtils.getRequest();
		
		ClientContext clientContext = new ClientContext();
		clientContext.setSessionId(request.getSession(true).getId());
		clientContext.setUser((GenericUser)user);

		return clientContext;
	}

	protected String[] getUserAuthorities(UserDetails user) {
		GrantedAuthority[] granthedAuthorities = user.getAuthorities();
		String[] authorities = new String[granthedAuthorities.length];

		int i = 0;
		for (GrantedAuthority grantedAuthority : granthedAuthorities) {
			System.out.println(grantedAuthority.getAuthority());
			authorities[i++] = grantedAuthority.getAuthority();
		}
		return authorities;
	}	

	public void logout() {
		loginUserService.logout();
	}

//	@Secured(RolePermissions.USER_MAINTENANCE_ROLE_PERMISSION_READ)
	public GenericUser getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
			return null;
		}
		
		if (authentication.getPrincipal() == null) {
			return null;
		}
		
		return (GenericUser)authentication.getPrincipal();
	}

//	@Override
//	public List<String> getAuditTrailedLoggedUsernames() {
//		sessionLogDao.loadUsers();
//		return null;
//	}
}
