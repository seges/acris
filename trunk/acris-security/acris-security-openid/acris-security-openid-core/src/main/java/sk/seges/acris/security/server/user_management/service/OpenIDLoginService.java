package sk.seges.acris.security.server.user_management.service;

import java.io.Serializable;
import java.util.List;

import sk.seges.acris.security.server.core.login.api.LoginService;
import sk.seges.acris.security.server.spring.user_management.dao.user.api.IGenericUserDao;
import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.user_management.server.model.data.OpenIDUserData;
import sk.seges.acris.security.server.utils.TokenConverter;
import sk.seges.acris.security.shared.exception.AuthenticationException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.session.SessionIDGenerator;
import sk.seges.acris.security.shared.user_management.domain.OpenIDLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.user_management.server.model.data.UserData;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;

public class OpenIDLoginService implements LoginService {

	private TokenConverter tokenConverter;

	private IGenericUserDao<UserData> userDao;

	private IOpenIDUserDao<? extends OpenIDUserData> openIDUserDao;

	private SessionIDGenerator sessionIDGenerator;

	public OpenIDLoginService(TokenConverter tokenConverter, IGenericUserDao<UserData> userDao,
			IOpenIDUserDao<? extends OpenIDUserData> openIDUserDao, SessionIDGenerator sessionIDGenerator) {
		this.tokenConverter = tokenConverter;
		this.userDao = userDao;
		this.openIDUserDao = openIDUserDao;
		this.sessionIDGenerator = sessionIDGenerator;
	}

	protected UserData convertToDTO(UserData source, LoginToken token) {
		UserData target = userDao.findByUsername(source.getUsername());

		return target;
	}

	protected UserData getUserByOpenIDIdentifier(OpenIDLoginToken token) {
		Page page = new Page(0, Page.ALL_RESULTS);
		page.setFilterable(new SimpleExpression<Comparable<? extends Serializable>>("id", token.getIdentifier(),
				Filter.EQ));
		List<OpenIDUserData> result = openIDUserDao.findAll(page).getResult();

		if (result != null && result.size() > 0) {
			return convertToDTO(result.get(0).getUser(),
					tokenConverter != null ? tokenConverter.convert(token, result.get(0).getUser()) : token);
		}
		return null;
	}

	@Override
	public ClientSession<UserData> login(LoginToken token) throws AuthenticationException {
		ClientSession<UserData> clientSession = new ClientSession<UserData>();
		clientSession.setSessionId(sessionIDGenerator.generate(token));

		if (token instanceof OpenIDLoginToken) {
			UserData user = getUserByOpenIDIdentifier((OpenIDLoginToken) token);
			if (user == null) {
				throw new AuthenticationException("User not found!");
			}
			clientSession.setUser(getUserByOpenIDIdentifier((OpenIDLoginToken) token));
		} else {
			throw new AuthenticationException("Unsupported login token type!");
		}

		return clientSession;
	}

	@Override
	public void logout() {
	}

	@Override
	public void changeAuthentication(ClientSession<UserData> clientSession) {
		// TODO Auto-generated method stub
	}
}