package sk.seges.acris.security.server.core.login.api;

import sk.seges.acris.security.shared.exception.AuthenticationException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

public interface LoginService {

	ClientSession login(LoginToken token) throws AuthenticationException;

	void logout();
}
