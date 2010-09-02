package sk.seges.acris.mvp.shared.result.user;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.sesam.dao.PagedResult;

import com.philbeaudoin.gwtp.dispatch.shared.Result;

public class FetchUsersResult implements Result {

	private static final long serialVersionUID = 1L;

	/**
	 * For serialization only.
	 */
	@SuppressWarnings("unused")
	private FetchUsersResult() {
	}

	private PagedResult<List<UserData>> users;

	public FetchUsersResult(PagedResult<List<UserData>> users) {
		this.users = users;
	}

	public PagedResult<List<UserData>> getUsers() {
		return users;
	}
}