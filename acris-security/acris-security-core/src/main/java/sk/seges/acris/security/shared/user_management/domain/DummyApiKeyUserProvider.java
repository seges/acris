package sk.seges.acris.security.shared.user_management.domain;

import java.util.ArrayList;

import sk.seges.acris.security.shared.apikey.ApiKeySession;
import sk.seges.acris.security.shared.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.acris.security.shared.user_management.domain.api.ApiKeyUserProvider;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

public class DummyApiKeyUserProvider implements ApiKeyUserProvider {

	@Override
	public UserData createUser(String apiKey) {
		UserData adminUser = (UserData) new HibernateGenericUser();
		adminUser.setEnabled(true);
		adminUser.setUserAuthorities(new ArrayList<String>());
		return adminUser;
	}

	@Override
	public ApiKeySession getSession(String apiKey) {
		return null;
	}
}
