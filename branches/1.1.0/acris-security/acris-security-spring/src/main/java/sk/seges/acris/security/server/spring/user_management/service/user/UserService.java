package sk.seges.acris.security.server.spring.user_management.service.user;

import javax.servlet.http.HttpSession;

import sk.seges.acris.security.server.core.login.api.LoginService;
import sk.seges.acris.security.server.core.login.api.LoginServiceProvider;
import sk.seges.acris.security.server.core.session.ServerSessionProvider;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.service.IUserService;
import sk.seges.acris.security.shared.util.LoginConstants;

public class UserService implements IUserService {

	private LoginServiceProvider loginServiceProvider;
	private ServerSessionProvider sessionProvider;

	public UserService(LoginServiceProvider loginServiceProvider, ServerSessionProvider sessionProvider) {
		this.loginServiceProvider = loginServiceProvider;
	}

	@Override
	public ClientSession login(LoginToken token) throws ServerException {
		LoginService loginService = loginServiceProvider.getLoginService(token);

		ClientSession clientSession = loginService.login(token);
		if (clientSession != null) {
			HttpSession session = sessionProvider.getSession(true);
			session.setAttribute(LoginConstants.LOGIN_TOKEN_NAME, token);
		}

		return clientSession;
	}

	@Override
	public void logout() throws ServerException {
		HttpSession session = sessionProvider.getSession(true);
		LoginToken token = (LoginToken) session.getAttribute(LoginConstants.LOGIN_TOKEN_NAME);

		loginServiceProvider.getLoginService(token).logout();
		session.removeAttribute(LoginConstants.LOGIN_TOKEN_NAME);
	}
}
