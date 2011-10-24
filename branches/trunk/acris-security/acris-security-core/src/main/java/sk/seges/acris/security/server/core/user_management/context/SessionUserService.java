package sk.seges.acris.security.server.core.user_management.context;

import javax.servlet.http.HttpSession;

import sk.seges.acris.security.server.core.session.ServerSessionProvider;
import sk.seges.acris.security.server.core.user_management.context.api.UserProviderService;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.user_management.context.SessionUserContext;
import sk.seges.acris.security.shared.user_management.domain.UserPasswordLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.util.LoginConstants;

public class SessionUserService implements UserProviderService {

	private ServerSessionProvider sessionProvider;
	
	public SessionUserService(ServerSessionProvider sessionProvider) {
		this.sessionProvider = sessionProvider;
	}
	
	@Override
	public String getLoggedUserName(UserContext userContext)
			throws ServerException {
		HttpSession session = sessionProvider.getSession();
		UserData<?> user = (UserData<?>) session.getAttribute(LoginConstants.LOGGED_USER_NAME);

		return user.getUsername();
	}

	@Override
	public UserData<?> getLoggedUser(UserContext userContext) {
		HttpSession session = sessionProvider.getSession();

		LoginToken token = (LoginToken) session.getAttribute(LoginConstants.LOGIN_TOKEN_NAME);
		if (token instanceof UserPasswordLoginToken && userContext instanceof SessionUserContext) {
			String sessionWebId = ((SessionUserContext) userContext).getWebId();
			String tokenWebId = ((UserPasswordLoginToken) token).getWebId();
			
			if (!sessionWebId.equals(tokenWebId)) return null;
		}
		
		UserData<?> user = (UserData<?>) session.getAttribute(LoginConstants.LOGGED_USER_NAME);

		return user;
	}
}
