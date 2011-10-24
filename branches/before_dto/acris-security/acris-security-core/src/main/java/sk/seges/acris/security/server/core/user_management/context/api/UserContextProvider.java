package sk.seges.acris.security.server.core.user_management.context.api;

import sk.seges.acris.security.shared.user_management.domain.api.UserContext;

public interface UserContextProvider {

	UserProviderService getUserProviderService(UserContext userContext);
}
