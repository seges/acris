package sk.seges.acris.security.server.core.login.api;

import sk.seges.acris.security.shared.exception.AuthenticationException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.user_management.server.model.data.UserData;

public interface LoginService {

	ClientSession<UserData> login(LoginToken token) throws AuthenticationException;

	void logout();

	void changeAuthentication(ClientSession<UserData> clientSession);
}
