package sk.seges.acris.security.server.user_management.service;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.core.login.api.LoginService;
import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.shared.exception.AuthenticationException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.session.SessionIDGenerator;
import sk.seges.acris.security.shared.user_management.domain.OpenIDLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.HasOpenIDIdentifier;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;

public class OpenIDLoginService implements LoginService {

	private IOpenIDUserDao<? extends HasOpenIDIdentifier> openIDUserDao;

	private SessionIDGenerator sessionIDGenerator;

	private Logger log = Logger.getLogger(OpenIDLoginService.class);

	public OpenIDLoginService(IOpenIDUserDao<? extends HasOpenIDIdentifier> openIDUserDao, SessionIDGenerator sessionIDGenerator) {
		this.openIDUserDao = openIDUserDao;
		this.sessionIDGenerator = sessionIDGenerator;
	}

	protected UserData<?> getUserByOpenIDIdentifier(String identifier) {
		Page page = new Page(0, 1);
		page.setFilterable(new SimpleExpression<Comparable<? extends Serializable>>("id", identifier, Filter.EQ));
		List<HasOpenIDIdentifier> result = openIDUserDao.findAll(page).getResult();
		
		if (result.size() > 0) {
			return (UserData<?>) result.get(0);
		}
		return null;
	}

	@Override
	@Transactional
	public ClientSession login(LoginToken token) throws AuthenticationException {
		ClientSession clientSession = new ClientSession();
		clientSession.setSessionId(sessionIDGenerator.generate(token));

		if (token instanceof OpenIDLoginToken) {
			clientSession.setUser(getUserByOpenIDIdentifier(((OpenIDLoginToken) token).getIdentifier()));
		} else {
			log.warn("Unsupported login token type!");
		}

		return clientSession;
	}

	@Override
	public void logout() {
	}
}
