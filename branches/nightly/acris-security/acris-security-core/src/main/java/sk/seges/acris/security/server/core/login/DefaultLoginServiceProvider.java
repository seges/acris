package sk.seges.acris.security.server.core.login;

import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.security.server.core.login.api.LoginService;
import sk.seges.acris.security.server.core.login.api.LoginServiceProvider;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

public class DefaultLoginServiceProvider implements LoginServiceProvider {

	public DefaultLoginServiceProvider(Class<? extends LoginToken> springLoginToken, LoginService springLoginService,
			Class<? extends LoginToken> openidLoginToken, LoginService openidLoginService) {
		serviceMap.put(springLoginToken, springLoginService);
		serviceMap.put(openidLoginToken, openidLoginService);
	}

	private Map<Class<? extends LoginToken>, LoginService> serviceMap = new HashMap<Class<? extends LoginToken>, LoginService>();

	@Override
	public LoginService getLoginService(LoginToken token) {
		return serviceMap.get(token.getClass());
	}

	public void registerLoginService(Class<? extends LoginToken> token, LoginService service) {
		serviceMap.put(token, service);
	}
}
