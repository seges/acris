package sk.seges.acris.security.server.user_management.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import sk.seges.acris.security.server.core.login.api.LoginService;
import sk.seges.acris.security.server.user_management.dao.IGrantsDao;
import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.utils.TokenConverter;
import sk.seges.acris.security.shared.exception.AuthenticationException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.session.SessionIDGenerator;
import sk.seges.acris.security.shared.user_management.domain.OpenIDLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.HasOpenIDIdentifier;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;

public class OpenIDLoginService implements LoginService {

	private TokenConverter tokenConverter;

	private IGrantsDao grantsDao;

	private IOpenIDUserDao<? extends HasOpenIDIdentifier> openIDUserDao;

	private SessionIDGenerator sessionIDGenerator;

	private Logger log = Logger.getLogger(OpenIDLoginService.class);

	public OpenIDLoginService(TokenConverter tokenConverter, IGrantsDao grantsDao,
			IOpenIDUserDao<? extends HasOpenIDIdentifier> openIDUserDao, SessionIDGenerator sessionIDGenerator) {
		this.tokenConverter = tokenConverter;
		this.grantsDao = grantsDao;
		this.openIDUserDao = openIDUserDao;
		this.sessionIDGenerator = sessionIDGenerator;
	}

	protected UserData<?> convertToDTO(UserData<?> source, LoginToken token) {
		UserData<Long> target = new GenericUserDTO();
		target.setEnabled(source.isEnabled());
		target.setId((Long) source.getId());
		target.setPassword(source.getPassword());
		target.setUsername(source.getUsername());

		List<String> authorities = grantsDao.findGrantsForToken(token);
		List<String> newAuthorities = new ArrayList<String>();
		newAuthorities.addAll(authorities);
		target.setUserAuthorities(newAuthorities);

		return target;
	}

	protected UserData<?> getUserByOpenIDIdentifier(OpenIDLoginToken token) {
		Page page = new Page(0, Page.ALL_RESULTS);
		page.setFilterable(new SimpleExpression<Comparable<? extends Serializable>>("id", token.getIdentifier(),
				Filter.EQ));
		List<HasOpenIDIdentifier> result = openIDUserDao.findAll(page).getResult();

		if (result != null && result.size() > 0) {
			return convertToDTO(result.get(0).getUser(),
					tokenConverter != null ? tokenConverter.convert(token, result.get(0).getUser()) : token);
		}
		return null;
	}

	@Override
	public ClientSession login(LoginToken token) throws AuthenticationException {
		ClientSession clientSession = new ClientSession();
		clientSession.setSessionId(sessionIDGenerator.generate(token));

		if (token instanceof OpenIDLoginToken) {
			clientSession.setUser(getUserByOpenIDIdentifier((OpenIDLoginToken) token));
		} else {
			log.warn("Unsupported login token type!");
		}

		return clientSession;
	}

	@Override
	public void logout() {
	}
}
