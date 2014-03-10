package sk.seges.acris.security.server.user_management;

import sk.seges.acris.security.server.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.acris.security.server.user_management.domain.api.ApiKeyUserProvider;
import sk.seges.acris.security.shared.apikey.ApiKeySession;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

import java.util.ArrayList;

public class DummyApiKeyUserProvider implements ApiKeyUserProvider {

	@Override
	public UserData createUser(String apiKey) {
		UserData adminUser = new HibernateGenericUser();
		adminUser.setEnabled(true);
		adminUser.setUserAuthorities(new ArrayList<String>());
		return adminUser;
	}

	@Override
	public ApiKeySession getSession(String apiKey) {
		return null;
	}
}
