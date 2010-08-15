package sk.seges.acris.mvp.shared.action;

import sk.seges.acris.mvp.shared.action.core.AbstractAction;
import sk.seges.acris.mvp.shared.result.AddUserResult;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;

public class AddUserAction extends AbstractAction<AddUserResult> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private AddUserAction() {
	}

	private GenericUser user;

	public AddUserAction(GenericUser user) {
		this.user = user;
	}

	public GenericUser getUser() {
		return user;
	}
}