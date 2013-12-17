package sk.seges.acris.security.server.user_management.service.spring;

import org.springframework.transaction.annotation.Transactional;
import sk.seges.acris.security.server.spring.user_management.dao.user.api.IGenericUserDao;
import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.user_management.server.model.data.OpenIDUserData;
import sk.seges.acris.security.server.user_management.service.OpenIDLoginService;
import sk.seges.acris.security.server.utils.TokenConverter;
import sk.seges.acris.security.shared.exception.AuthenticationException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.session.SessionIDGenerator;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

public class SpringOpenIDLoginService extends OpenIDLoginService {

	public SpringOpenIDLoginService(TokenConverter tokenConverter, IGenericUserDao<UserData> userDao,
			IOpenIDUserDao<OpenIDUserData> openIDUserDao, SessionIDGenerator sessionIDGenerator) {
		super(tokenConverter, userDao, openIDUserDao, sessionIDGenerator);
	}

	@Transactional
	@Override
	public ClientSession<UserData> login(LoginToken token) throws AuthenticationException {
		return super.login(token);
	}
}
