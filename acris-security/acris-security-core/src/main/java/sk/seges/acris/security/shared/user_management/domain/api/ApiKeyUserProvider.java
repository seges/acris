package sk.seges.acris.security.shared.user_management.domain.api;

import sk.seges.corpis.server.domain.user.server.model.data.UserData;

public interface ApiKeyUserProvider {

	UserData createUser(String apiKey);
}
