package sk.seges.acris.security.server.user_management.service.spring;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.user_management.dao.IGrantsDao;
import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.user_management.service.OpenIDLoginService;
import sk.seges.acris.security.server.utils.TokenConverter;
import sk.seges.acris.security.shared.exception.AuthenticationException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.session.SessionIDGenerator;
import sk.seges.acris.security.shared.user_management.domain.api.HasOpenIDIdentifier;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

public class SpringOpenIDLoginService extends OpenIDLoginService {

	public SpringOpenIDLoginService(TokenConverter tokenConverter, IGrantsDao grantsDao,
			IOpenIDUserDao<HasOpenIDIdentifier> openIDUserDao, SessionIDGenerator sessionIDGenerator) {
		super(tokenConverter, grantsDao, openIDUserDao, sessionIDGenerator);
	}

	@Transactional
	@Override
	public ClientSession login(LoginToken token) throws AuthenticationException {
		return super.login(token);
	}
}
