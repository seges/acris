package sk.seges.acris.security.server.spring.login;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import sk.seges.acris.security.server.spring.context.AcrisSecurityContext;
import sk.seges.acris.security.server.spring.user_management.dao.user.api.IGenericUserDao;
import sk.seges.acris.security.server.spring.user_management.service.provider.WebIdAnonymousAuthenticationToken;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.server.session.SessionIDGenerator;
import sk.seges.acris.security.server.spring.user_management.domain.SpringAuthoritiesSupport;
import sk.seges.acris.security.shared.user_management.domain.AnonymousLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

public class AnonymousLoginService extends SpringLoginService {

	private SpringAuthoritiesSupport authoritiesSupport;
	private String anonymousKey;
	private IGenericUserDao<UserData> userDao;
	
	public AnonymousLoginService(AuthenticationManager authenticationManager, SessionIDGenerator sessionIDGenerator, String anonymousKey,
			IGenericUserDao<UserData> userDao) {
		super(authenticationManager, sessionIDGenerator);
		this.anonymousKey = anonymousKey;
		this.userDao = userDao;
		authoritiesSupport = new SpringAuthoritiesSupport();
	}
	
	@Override
	protected Authentication createAuthenticationToken(LoginToken token) throws ServerException {
		assert (token instanceof AnonymousLoginToken);
		//load user to initialize authorities
		UserData user = userDao.findUser(token.getWebId(), token.getWebId());
		return new WebIdAnonymousAuthenticationToken(anonymousKey, user, token.getWebId(), 
				authoritiesSupport.convertAuthorities(user.getUserAuthorities()));
	}
	
	protected void createSecurityContext(Authentication auth) {
		Authentication newAuth = new AnonymousAuthenticationToken(anonymousKey, auth.getPrincipal(),
				auth.getAuthorities());
		AcrisSecurityContext sc = new AcrisSecurityContext();
		sc.setAuthentication(newAuth);
		SecurityContextHolder.setContext(sc);
	}
}
