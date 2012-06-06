package sk.seges.acris.security.server.spring.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.core.login.api.LoginService;
import sk.seges.acris.security.server.spring.context.AcrisSecurityContext;
import sk.seges.acris.security.shared.exception.AuthenticationException;
import sk.seges.acris.security.shared.exception.SecurityException;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.session.SessionIDGenerator;
import sk.seges.acris.security.shared.spring.user_management.domain.SpringUserAdapter;
import sk.seges.acris.security.shared.user_management.domain.UserPasswordLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.RoleData;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.util.LoggedUserRole;

/**
 * Standard user service using {@link UserPasswordLoginToken} to log the user in
 * (and out). The service uses Spring's DAO authentication provider. The
 * behaviour of fetching authorities can be altered by:
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
public class SpringLoginService implements LoginService {

	private AuthenticationManager authenticationManager;

	private SessionIDGenerator sessionIDGenerator;

	private Logger log = Logger.getLogger(SpringLoginService.class);

	public SpringLoginService(AuthenticationManager authenticationManager, SessionIDGenerator sessionIDGenerator) {
		this.authenticationManager = authenticationManager;
		this.sessionIDGenerator = sessionIDGenerator;
	}

	protected String[] getUserAuthorities(UserDetails user) {
		GrantedAuthority[] granthedAuthorities = user.getAuthorities();
		String[] authorities = new String[granthedAuthorities.length];

		int i = 0;
		for (GrantedAuthority grantedAuthority : granthedAuthorities) {
			authorities[i++] = grantedAuthority.getAuthority();
		}
		return authorities;
	}

	/**
	 * Transforms {@link LoginToken} to {@link Authentication} token.
	 * Authentication token is required by spring-security authentication
	 * manager to authenticate a principal.
	 * 
	 * @param token
	 *            Client's LoginToken representing information required to
	 *            successfully authenticate him.
	 * @return Spring-security's Authentication token.
	 * @throws ServerException
	 */
	protected Authentication createAuthenticationToken(LoginToken token) throws ServerException {
		assert (token instanceof UserPasswordLoginToken);
		UserPasswordLoginToken loginToken = (UserPasswordLoginToken) token;
		return new UsernamePasswordAuthenticationToken(loginToken.getUsername(), loginToken.getPassword());
	}

	/**
	 * Allows to extend client session instance with custom one (usually
	 * extended from {@link ClientSession}). To successfully pass persistent
	 * entities you have to provide own (extended) client session implementation
	 * with respective getters & setters + override return value in your user
	 * service implementation's login method.
	 * 
	 * @return
	 */
	protected ClientSession createClientSession() {
		return new ClientSession();
	}

	/**
	 * Extension point to enrich client session with custom properties.
	 * 
	 * @param clientSession
	 */
	protected void postProcessLogin(ClientSession clientSession, LoginToken token) {
	}

	@Transactional
	public ClientSession login(LoginToken token) throws ServerException {
		Authentication auth;
		try {
			auth = authenticationManager.authenticate(createAuthenticationToken(token));
		} catch (AuthenticationException e) {
			throw new SecurityException("Unable to login", e);
		}
		createSecurityContext(auth);

		ClientSession clientSession = createClientSession();
		clientSession.setSessionId(sessionIDGenerator.generate(token));

		List<RoleData> roles = new ArrayList<RoleData>();
		roles.add(new LoggedUserRole());
		if (auth.getPrincipal() instanceof SpringUserAdapter) {
			SpringUserAdapter<?> adapter = (SpringUserAdapter<?>) auth.getPrincipal();
			adapter.setRoles(roles);
			clientSession.setUser(adapter.getUser());
		} else if (auth.getPrincipal() instanceof UserData) {
			UserData<?> userData = (UserData<?>) auth.getPrincipal();
			userData.setRoles(roles);
			clientSession.setUser(userData);
		} else {
			if (auth.getPrincipal() == null) {
				log.warn("Null principal in the security context. Invalid state occured. Please provide valid principal.");
			} else {
				log.warn("Unsupported type of the principal. Class "
						+ auth.getPrincipal().getClass().getCanonicalName() + " is not supported!");
			}
		}

		postProcessLogin(clientSession, token);
		return clientSession;
	}

	private void createSecurityContext(Authentication auth) {
		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), auth.getAuthorities());
		AcrisSecurityContext sc = new AcrisSecurityContext();
		sc.setAuthentication(newAuth);
		SecurityContextHolder.setContext(sc);
	}
	
	/**
	 * @see sk.seges.acris.security.rpc.user_management.service.IUserService#logout()
	 */
	@Override
	public void logout() {
		SecurityContextHolder.clearContext();
	}
}
