package sk.seges.acris.security.server.core.login.api;

import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

public interface LoginServiceProvider {

	LoginService getLoginService(LoginToken token);
}
