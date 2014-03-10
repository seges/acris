package sk.seges.acris.security.server.user_management.domain;

import sk.seges.acris.security.server.spring.user_management.dao.user.api.IGenericUserDao;
import sk.seges.acris.security.shared.apikey.ApiKeySession;
import sk.seges.acris.security.shared.apikey.ApiKeySessionHolder;
import sk.seges.acris.security.server.user_management.domain.api.ApiKeyUserProvider;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;

public class ApiKeySessionUserProvider implements ApiKeyUserProvider {
	
	protected IGenericUserDao<UserData> userDao;
	private ApiKeySessionHolder apiKeySessionHolder;
	
	public ApiKeySessionUserProvider(IGenericUserDao<UserData> userDao, ApiKeySessionHolder apiKeySessionHolder) {
		this.userDao = userDao;
		this.apiKeySessionHolder = apiKeySessionHolder;
	}
	
	@Override
	public UserData createUser(String apiKey) {
		ApiKeySession session = getSession(apiKey);
		
		if (session != null) {
			Page p = new Page(0, Page.ALL_RESULTS);
			p.setFilterable(Filter.eq(UserData.ID, session.getUserId()));
			return userDao.findUnique(p);
		}
		return null;
	}

	@Override
	public ApiKeySession getSession(String apiKey) {
		return apiKeySessionHolder.getSession(apiKey);
	}
}
