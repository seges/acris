package sk.seges.acris.mvp.client.action.mocks;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.mvp.client.action.mocks.core.MockActionHandler;
import sk.seges.acris.mvp.shared.action.user.LoginAction;
import sk.seges.acris.mvp.shared.result.user.LoginResult;
import sk.seges.acris.mvp.shared.security.Grants;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.SecurityConstants;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;

import com.google.inject.Inject;

public class MockLoginActionHandler extends MockActionHandler<LoginAction, LoginResult> {

	private static final String MOCK_SESSION_ID = "ABCDEFGHIJKL";
	
	@Inject
	public MockLoginActionHandler() {	
	}

	@Override
	public LoginResult execute(LoginAction action) {
		ClientSession clientSession = new ClientSession();
		clientSession.setSessionId(MOCK_SESSION_ID);
		
		GenericUserDTO user = new GenericUserDTO();
		List<String> userAuthorities = new ArrayList<String>();
		userAuthorities.add(SecurityConstants.AUTH_PREFIX + Grants.USER_MAINTENANCE);
		user.setUserAuthorities(userAuthorities);
		clientSession.setUser(user);
		LoginResult result = new LoginResult(clientSession);
		return result;
	}

	@Override
	protected Class<LoginAction> getActionType() {
		return LoginAction.class;
	}
	
}
