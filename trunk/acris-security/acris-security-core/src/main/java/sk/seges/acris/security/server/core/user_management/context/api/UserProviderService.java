package sk.seges.acris.security.server.core.user_management.context.api;

import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;
import sk.seges.acris.security.user_management.server.model.data.UserData;

public interface UserProviderService {

	String getLoggedUserName(UserContext userContext) throws ServerException;
	
	ClientSession<UserData> getLoggedSession(UserContext userContext);
	
}