package sk.seges.acris.security.server.core.login.api;

import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

public interface PostProcessLogin {

	void postProcessLogin(ClientSession<UserData> clientSession, LoginToken token);
	
	void postProcessLogin(ClientSession<UserData> clientSession, UserContext userContext);
}
