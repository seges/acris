package sk.seges.acris.security.server.core.login;

import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.security.server.core.login.api.LoginService;
import sk.seges.acris.security.server.core.login.api.LoginServiceProvider;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

public class DefaultLoginServiceProvider implements LoginServiceProvider {

	private Map<LoginToken, LoginService> serviceMap = new HashMap<LoginToken, LoginService>();

	@Override
	public LoginService getLoginService(LoginToken token) {
		return serviceMap.get(token);
	}

	public void registerLoginService(LoginToken token, LoginService service) {
		serviceMap.put(token, service);
	}
}
