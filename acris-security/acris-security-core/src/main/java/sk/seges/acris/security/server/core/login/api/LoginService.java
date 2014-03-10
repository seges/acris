package sk.seges.acris.security.server.core.login.api;

import sk.seges.acris.security.shared.exception.AuthenticationException;
import sk.seges.acris.security.server.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

public interface LoginService {

	ClientSession login(LoginToken token) throws AuthenticationException;

	void logout();

	void changeAuthentication(ClientSession clientSession);
}
