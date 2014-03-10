package sk.seges.acris.security.server.user_management.domain.api;

import sk.seges.acris.security.shared.apikey.ApiKeySession;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

public interface ApiKeyUserProvider {

	UserData createUser(String apiKey);
	
	ApiKeySession getSession(String apiKey);
}
