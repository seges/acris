package sk.seges.acris.security.server.core.login.api;

import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.user_management.server.model.data.UserData;

public interface PostProcessLogin {

	void postProcessLogin(ClientSession<UserData> clientSession, LoginToken token);
}
