package sk.seges.acris.security.server.user_management.service.spring;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.user_management.service.OpenIDLoginService;
import sk.seges.acris.security.shared.session.SessionIDGenerator;
import sk.seges.acris.security.shared.user_management.domain.api.HasOpenIDIdentifier;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class SpringOpenIDLoginService extends OpenIDLoginService {

	public SpringOpenIDLoginService(IOpenIDUserDao<HasOpenIDIdentifier> openIDUserDao,
			SessionIDGenerator sessionIDGenerator) {
		super(openIDUserDao, sessionIDGenerator);
	}

	@Transactional
	@Override
	protected UserData<?> getUserByOpenIDIdentifier(String identifier) {
		return super.getUserByOpenIDIdentifier(identifier);
	}
}
