package sk.seges.acris.security.server.core.user_management.context.api;

import sk.seges.acris.security.server.session.ClientSession;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;

public interface UserProviderService {

	String getLoggedUserName(UserContext userContext) throws ServerException;
	
	ClientSession getLoggedSession(UserContext userContext);
	
}