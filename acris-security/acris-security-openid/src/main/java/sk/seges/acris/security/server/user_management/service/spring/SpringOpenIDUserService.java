package sk.seges.acris.security.server.user_management.service.spring;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.user_management.service.OpenIDUserService;
import sk.seges.acris.security.shared.user_management.domain.api.HasOpenIDIdentifier;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class SpringOpenIDUserService extends OpenIDUserService {

	public SpringOpenIDUserService() {
		super(null);
	}

	public SpringOpenIDUserService(IOpenIDUserDao<? extends HasOpenIDIdentifier> openIDUserDao) {
		super(openIDUserDao);
	}

	@Transactional
	@Override
	public UserData<?> getUserByOpenIDIdentifier(String identifier) {
		return super.getUserByOpenIDIdentifier(identifier);
	}

	@Transactional
	@Override
	public List<OpenIDProvider> findProvidersByUserName(String userName) {
		return super.findProvidersByUserName(userName);
	}

	@Transactional
	@Override
	public void saveUserByIdentifiers(UserData<?> user, Map<String, Object> identifiers) {
		super.saveUserByIdentifiers(user, identifiers);
	}
}
