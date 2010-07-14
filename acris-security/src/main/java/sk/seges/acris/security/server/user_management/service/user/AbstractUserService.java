/**
 * 
 */
package sk.seges.acris.security.server.user_management.service.user;

import net.sf.gilead.gwt.PersistentRemoteService;

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.context.SecurityContextImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.exception.SecurityException;
import sk.seges.acris.security.rpc.exception.ServerException;
import sk.seges.acris.security.rpc.session.ClientSession;
import sk.seges.acris.security.rpc.session.SessionIDGenerator;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.acris.security.rpc.user_management.domain.LoginToken;
import sk.seges.acris.security.rpc.user_management.service.IUserService;

/**
 * User service base for various implementations using acris-security logic. It
 * allows you to use own LoginToken, Authentication token, enrich client session
 * or enforce own session ID generation policy.
 * 
 * @author ladislav.gazo
 */
public abstract class AbstractUserService extends PersistentRemoteService implements IUserService {
	private static final long serialVersionUID = -4227745445378198727L;

	private AuthenticationManager authenticationManager;

	private SessionIDGenerator sessionIDGenerator;

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setSessionIDGenerator(SessionIDGenerator sessionIDGenerator) {
		this.sessionIDGenerator = sessionIDGenerator;
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
	protected abstract Authentication createAuthenticationToken(LoginToken token) throws ServerException;

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
	protected void postProcessLogin(ClientSession clientSession) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sk.seges.acris.security.rpc.user_management.service.IUserService#login
	 * (sk.seges.acris.security.rpc.user_management.domain.LoginToken)
	 */
	@Override
	@Transactional
	public ClientSession login(LoginToken token) throws ServerException {
		Authentication auth;
		try {
			auth = authenticationManager.authenticate(createAuthenticationToken(token));
		} catch (AuthenticationException e) {
			throw new SecurityException("Unable to login", e);
		}
		SecurityContext sc = new SecurityContextImpl();
		sc.setAuthentication(auth);

		SecurityContextHolder.setContext(sc);

		ClientSession clientSession = createClientSession();
		clientSession.setSessionId(sessionIDGenerator.generate(token));
		clientSession.setUser((UserDetails)auth.getPrincipal());

		postProcessLogin(clientSession);
		return clientSession;
	}
	
	/*
	 * (non-Javadoc)
	 * @see sk.seges.acris.security.rpc.user_management.service.IUserService#logout()
	 */
	@Override
	public void logout() {
		SecurityContextHolder.clearContext();
	}
	
	/*(
	 * (non-Javadoc)
	 * @see sk.seges.acris.security.rpc.user_management.service.IUserService#getLoggedUser()
	 */
	@Override
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
}
