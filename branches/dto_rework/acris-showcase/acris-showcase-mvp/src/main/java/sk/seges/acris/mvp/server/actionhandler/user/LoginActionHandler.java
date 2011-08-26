package sk.seges.acris.mvp.server.actionhandler.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sk.seges.acris.mvp.server.actionhandler.core.DefaultActionHandler;
import sk.seges.acris.mvp.server.service.dozer.DozerSupport;
import sk.seges.acris.mvp.shared.action.user.LoginAction;
import sk.seges.acris.mvp.shared.result.user.LoginResult;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.UserPasswordLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.service.IUserService;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class LoginActionHandler extends DefaultActionHandler<LoginAction, LoginResult> {

	@Autowired
	private IUserService userDetailsService;

	@Autowired
	private DozerSupport dozer;
	
	@Override
	public Class<LoginAction> getActionType() {
		return LoginAction.class;
	}

	@Override
	public LoginResult execute(LoginAction action, ExecutionContext context) throws ActionException {
		ClientSession session = userDetailsService.login(new UserPasswordLoginToken(action.getUsername(), action.getPassword(), ""));
		UserData<?> userData = dozer.convert(session.getUser());
		session.setUser(userData);
		return new LoginResult(session);
	}

	@Override
	public void undo(LoginAction action, LoginResult result, ExecutionContext context) throws ActionException {
		userDetailsService.logout();
	}
}