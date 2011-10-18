package sk.seges.acris.security.server.spring.user_management.service.user;

import javax.servlet.http.HttpSession;

import sk.seges.acris.security.server.core.login.api.LoginService;
import sk.seges.acris.security.server.core.login.api.LoginServiceProvider;
import sk.seges.acris.security.server.core.session.ServerSessionProvider;
import sk.seges.acris.security.server.core.user_management.context.api.UserContextProvider;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.service.IUserService;
import sk.seges.acris.security.shared.util.LoginConstants;

public class UserService implements IUserService {

	private LoginServiceProvider loginServiceProvider;
	private ServerSessionProvider sessionProvider;
	private UserContextProvider userContextProvider;
	
	public UserService(LoginServiceProvider loginServiceProvider, ServerSessionProvider sessionProvider, UserContextProvider userContextProvider) {
		this.loginServiceProvider = loginServiceProvider;
		this.sessionProvider = sessionProvider;
		this.userContextProvider = userContextProvider;
	}

	@Override
	public String authenticate(LoginToken token) throws ServerException {
		return login(token).getSessionId();
	}

	@Override
	public ClientSession login(LoginToken token) throws ServerException {
		LoginService loginService = loginServiceProvider.getLoginService(token);

		ClientSession clientSession = loginService.login(token);
		if (clientSession != null) {
			HttpSession session = sessionProvider.getSession();
			session.setAttribute(LoginConstants.LOGIN_TOKEN_NAME, token);
			session.setAttribute(LoginConstants.LOGGED_USER_NAME, clientSession.getUser());
		}

		return clientSession;
	}

	@Override
	public void logout() throws ServerException {
		HttpSession session = sessionProvider.getSession();
		LoginToken token = (LoginToken) session.getAttribute(LoginConstants.LOGIN_TOKEN_NAME);

		if (token != null) {
			loginServiceProvider.getLoginService(token).logout();
			session.removeAttribute(LoginConstants.LOGIN_TOKEN_NAME);
			session.removeAttribute(LoginConstants.LOGGED_USER_NAME);
		}
	}

	@Override
	public String getLoggedUserName(UserContext userContext) throws ServerException {
		return userContextProvider.getUserProviderService(userContext).getLoggedUserName(userContext);
	}
	
	@Override
	public UserData<?> getLoggedUser(UserContext userContext) {
		return userContextProvider.getUserProviderService(userContext).getLoggedUser(userContext);
	}
}
