package sk.seges.acris.mvp.shared.result;

import java.util.List;

import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
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

	private PagedResult<List<GenericUser>> users;

	public FetchUsersResult(PagedResult<List<GenericUser>> users) {
		this.users = users;
	}

	public PagedResult<List<GenericUser>> getUsers() {
		return users;
	}
}