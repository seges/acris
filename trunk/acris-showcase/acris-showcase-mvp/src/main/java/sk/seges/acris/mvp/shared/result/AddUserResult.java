package sk.seges.acris.mvp.shared.result;

import sk.seges.acris.security.rpc.user_management.domain.GenericUser;

import com.philbeaudoin.gwtp.dispatch.shared.Result;

public class AddUserResult implements Result {

	private static final long serialVersionUID = 1L;

	/**
	 * For serialization only.
	 */
	@SuppressWarnings("unused")
	private AddUserResult() {
	}

	private GenericUser user;

	public AddUserResult(GenericUser user) {
		this.user = user;
	}

	public GenericUser getUser() {
		return user;
	}
}