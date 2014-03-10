package sk.seges.acris.security.server.core.login.api;

import sk.seges.acris.security.server.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;

public interface PostProcessLogin {

	void postProcessLogin(ClientSession clientSession, LoginToken token);
	
	void postProcessLogin(ClientSession clientSession, UserContext userContext);
}
