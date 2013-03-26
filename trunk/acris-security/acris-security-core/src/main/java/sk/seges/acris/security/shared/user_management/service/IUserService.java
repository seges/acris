package sk.seges.acris.security.shared.user_management.service;

import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import com.google.gwt.user.client.rpc.RemoteService;

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
	 * 
	 * @param username
	 *            unique username of the user
	 * @param password
	 *            plain text user password. In the future it should no be a
	 *            plain text
	 * @param language
	 *            user selected language represented as string (e.g. "sk",
	 *            "en_US")
	 * @return ClientSession with logged user and session id
	 */
	ClientSession<GenericUserDTO> login(LoginToken token) throws ServerException;

	/**
	 * Logs out current logged user.
	 * 
	 */
	void logout() throws ServerException;

	String getLoggedUserName(UserContext userContext) throws ServerException;
	
	ClientSession<GenericUserDTO> getLoggedSession(UserContext userContext);

	void changeAuthentication(ClientSession<GenericUserDTO> clientSession);
}