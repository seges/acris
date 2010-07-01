package sk.seges.acris.security.rpc.user_management.service;

import sk.seges.acris.security.rpc.exception.ServerException;
import sk.seges.acris.security.rpc.session.ClientSession;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.acris.security.rpc.user_management.domain.LoginToken;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * User service used to basic authentication functionality such as login and
 * logout. Additionally service also provides other functionality related to the
 * users.
 * 
 * @author fat
 */
public interface IUserService extends RemoteService {

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
	ClientSession login(LoginToken token) throws ServerException;

	/**
	 * Logs out current logged user.
	 * 
	 */
	void logout() throws ServerException;

	/**
	 * @return current logged user from user server session. If no user is
	 *         logged return value is null
	 */
	GenericUser getLoggedUser() throws ServerException;
}