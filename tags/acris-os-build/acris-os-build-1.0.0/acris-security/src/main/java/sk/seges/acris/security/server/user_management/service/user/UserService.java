package sk.seges.acris.security.server.user_management.service.user;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.userdetails.UserDetails;

import sk.seges.acris.security.rpc.exception.ServerException;
import sk.seges.acris.security.rpc.user_management.domain.LoginToken;
import sk.seges.acris.security.rpc.user_management.domain.UserPasswordLoginToken;

/**
 * Standard user service using {@link UserPasswordLoginToken} to log the user in
 * (and out). The service uses Spring's DAO authentication provider.
 * 
 * The behaviour of fetching authorities can be altered by:
 * <ul>
 * <li>providing specific implementation of generic user DAO (bean name is
 * 'genericUserDao'). By aliasing the bean name to the specific implementation
 * you are able to provide authorities for the user with own DAO.</li>
 * <li>providing own chain of authentication providers</li>
 * </ul>
 * 
 * @author fat
 * @author ladislav.gazo
 */
public class UserService extends AbstractUserService {

	// @Autowired
	// private ISessionLogDAO sessionLogDao;

	private static final long serialVersionUID = 4347689255879212323L;

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

	@Override
	protected Authentication createAuthenticationToken(LoginToken token) throws ServerException {
		assert (token instanceof UserPasswordLoginToken);
		UserPasswordLoginToken loginToken = (UserPasswordLoginToken) token;
		return new UsernamePasswordAuthenticationToken(loginToken.getUsername(), loginToken.getPassword());
	}

	// @Override
	// protected void postProcessLogin(ClientSession clientSession) {
	// super.postProcessLogin(clientSession);

	// if (userDetail instanceof SessionLog) {
	// SessionLog sessionLog = (SessionLog)userDetail;
	// sessionLog.setUser((GenericUser)user);
	// sessionLogDao.add(sessionLog);
	// }
	// }

	// @Override
	// public List<String> getAuditTrailedLoggedUsernames() {
	// sessionLogDao.loadUsers();
	// return null;
	// }
}
