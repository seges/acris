package sk.seges.acris.mvp.shared.action.user;

import sk.seges.acris.mvp.shared.action.core.AbstractAction;
import sk.seges.acris.mvp.shared.result.user.AddUserResult;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class AddUserAction extends AbstractAction<AddUserResult> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private AddUserAction() {
	}

	private UserData user;

	public AddUserAction(UserData user) {
		this.user = user;
	}

	public UserData getUser() {
		return user;
	}
}