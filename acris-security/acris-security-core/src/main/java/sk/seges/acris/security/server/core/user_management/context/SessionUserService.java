package sk.seges.acris.security.server.core.user_management.context;

import sk.seges.acris.security.server.core.session.ServerSessionProvider;
import sk.seges.acris.security.server.core.user_management.context.api.UserProviderService;
import sk.seges.acris.security.server.session.ClientSession;
import sk.seges.acris.security.server.util.LoginConstants;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.user_management.context.SessionUserContext;
import sk.seges.acris.security.shared.user_management.domain.UserPasswordLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

import javax.servlet.http.HttpSession;

public class SessionUserService implements UserProviderService {

	private ServerSessionProvider sessionProvider;
	
	public SessionUserService(ServerSessionProvider sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	@SuppressWarnings("unchecked")
	private ClientSession getClientSession() {
		HttpSession session = sessionProvider.getSession();
		return (ClientSession) session.getAttribute(LoginConstants.CLIENT_SESSION_NAME);
	}
	
	private UserData getLoggedUser() {
		ClientSession clientSession = getClientSession();
		
		if (clientSession != null) {
			return clientSession.getUser();
		}
		
		return null;
	}
	
	@Override
	public String getLoggedUserName(UserContext userContext)
			throws ServerException {
		UserData user = getLoggedUser();

		if (user != null) {
			return user.getUsername();
		}
		
		return null;
	}

	@Override
	public ClientSession getLoggedSession(UserContext userContext) {
		HttpSession session = sessionProvider.getSession();

		LoginToken token = (LoginToken) session.getAttribute(LoginConstants.LOGIN_TOKEN_NAME);
		if (token instanceof UserPasswordLoginToken && userContext instanceof SessionUserContext) {
			String sessionWebId = userContext.getWebId();
			String tokenWebId = token.getWebId();
			
			if (!sessionWebId.equals(tokenWebId)) return null;
		}

		return getClientSession();
	}
}