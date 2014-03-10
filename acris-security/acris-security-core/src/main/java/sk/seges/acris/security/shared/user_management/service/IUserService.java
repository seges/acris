package sk.seges.acris.security.shared.user_management.service;

import com.google.gwt.user.client.rpc.RemoteService;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.session.ClientSessionDTO;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

/**
 * User service used to basic authentication functionality such as login and
 * logout. Additionally service also provides other functionality related to the
 * users.
 * 
 * @author fat
 */
@RemoteServiceDefinition
public interface IUserService extends RemoteService {

	/**
	 * Logs in and returns sessionId only, using URL redirection.
	 * 
	 * @param token
	 * @return
	 */
	String authenticate(LoginToken token) throws ServerException;

	/**
	 * Authenticate user represented with username and his credentials. Service
	 * method also set selected language to user preferences
	 * @return ClientSession with logged user and session id
	 */
	ClientSessionDTO login(LoginToken token) throws ServerException;

	/**
	 * Logs out current logged user.
	 * 
	 */
	void logout() throws ServerException;

	String getLoggedUserName(UserContext userContext) throws ServerException;

	ClientSessionDTO getLoggedSession(UserContext userContext);

	void changeAuthentication(ClientSessionDTO clientSession);
}