package sk.seges.acris.security.server.core.user_management.context;

import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.security.server.core.user_management.context.api.UserContextProvider;
import sk.seges.acris.security.server.core.user_management.context.api.UserProviderService;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;

public class DefaultUserContextProvider implements  UserContextProvider {

	private Map<Class<? extends UserContext>, UserProviderService> serviceMap = new HashMap<Class<? extends UserContext>, UserProviderService>();
	
	public DefaultUserContextProvider(Class<? extends UserContext> sessionUserContext, UserProviderService sessionLoginService,
			Class<? extends UserContext> apiKeyUserContext, UserProviderService apiKeyLoginService) {
		serviceMap.put(sessionUserContext, sessionLoginService);
		serviceMap.put(apiKeyUserContext, apiKeyLoginService);
	}

	@Override
	public UserProviderService getUserProviderService(UserContext userContext) {
		return serviceMap.get(userContext.getClass());
	}

	public void registerLoginService(Class<? extends UserContext> userContext, UserProviderService service) {
		serviceMap.put(userContext, service);
	}
}
