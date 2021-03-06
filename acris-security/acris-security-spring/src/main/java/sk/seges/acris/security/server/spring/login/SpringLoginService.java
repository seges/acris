package sk.seges.acris.security.server.spring.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.core.login.api.LoginService;
import sk.seges.acris.security.server.session.ClientSession;
import sk.seges.acris.security.server.session.SessionIDGenerator;
import sk.seges.acris.security.server.spring.context.AcrisSecurityContext;
import sk.seges.acris.security.server.spring.user_management.domain.SpringUserAdapter;
import sk.seges.acris.security.server.spring.user_management.service.provider.WebIdUsernamePasswordAuthenticationToken;
import sk.seges.acris.security.server.util.LoggedUserRole;
import sk.seges.acris.security.shared.exception.AuthenticationException;
import sk.seges.acris.security.shared.exception.SecurityException;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.user_management.domain.UserPasswordLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

/**
 * Standard user service using {@link UserPasswordLoginToken} to log the user in
 * (and out). The service uses Spring's DAO authentication provider. The
 * behavior of fetching authorities can be altered by:
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
		Collection<? extends GrantedAuthority> granthedAuthorities = user.getAuthorities();
		String[] authorities = new String[granthedAuthorities.size()];

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
		return new WebIdUsernamePasswordAuthenticationToken(loginToken.getUsername(), loginToken.getPassword(), loginToken.getWebId());
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

	@Override
	public void changeAuthentication(ClientSession clientSession) {
		UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userData.setRoles(clientSession.getUser().getRoles());
		Authentication oldAuth = SecurityContextHolder.getContext().getAuthentication();
		Authentication newAuth = new UsernamePasswordAuthenticationToken(userData, oldAuth
				.getCredentials(), oldAuth.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

	/**
	 * Extension point to enrich client session with custom properties.
	 * 
	 * @param clientSession
	 */
	public void postProcessLogin(ClientSession clientSession, LoginToken token) {}

	@Override
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
			SpringUserAdapter adapter = (SpringUserAdapter) auth.getPrincipal();
			if (adapter.getRoles() == null || adapter.getRoles().isEmpty()) {
				adapter.setRoles(roles);
			} else {
				adapter.getRoles().addAll(roles);
			}
			clientSession.setUser(adapter.getUser());
		} else if (auth.getPrincipal() instanceof UserData) {
			UserData userData = (UserData) auth.getPrincipal();
			if (userData.getRoles() == null || userData.getRoles().isEmpty()) {
				userData.setRoles(roles);
			} else {
				userData.getRoles().addAll(roles);
			}
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

	protected void createSecurityContext(Authentication auth) {
		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(),
				auth.getAuthorities());
		AcrisSecurityContext sc = new AcrisSecurityContext();
		sc.setAuthentication(newAuth);
		SecurityContextHolder.setContext(sc);
	}

	@Override
	public void logout() {
		SecurityContextHolder.clearContext();
		SecurityContextHolder.setContext(new AcrisSecurityContext());
	}
}
