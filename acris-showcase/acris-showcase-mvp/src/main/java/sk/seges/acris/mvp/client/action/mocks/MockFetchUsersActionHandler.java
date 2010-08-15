package sk.seges.acris.mvp.client.action.mocks;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.mvp.client.action.mocks.core.MockActionHandler;
import sk.seges.acris.mvp.client.action.mocks.factory.UsersFactory;
import sk.seges.acris.mvp.shared.action.FetchUsersAction;
import sk.seges.acris.mvp.shared.result.FetchUsersResult;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.sesam.dao.PagedResult;

import com.google.inject.Inject;

public class MockFetchUsersActionHandler extends MockActionHandler<FetchUsersAction, FetchUsersResult> {

	@Inject
	public MockFetchUsersActionHandler() {
	}
	
	@Override
	public FetchUsersResult execute(FetchUsersAction action) {
		GenericUser user = UsersFactory.createMockUser(1L, "test", "test");

		PagedResult<List<GenericUser>> pagedUsers = new PagedResult<List<GenericUser>>();
		List<GenericUser> users = new ArrayList<GenericUser>();
		users.add(user);
		pagedUsers.setResult(users);

		return new FetchUsersResult(pagedUsers);
	}

	@Override
	public Class<FetchUsersAction> getActionType() {
		return FetchUsersAction.class;
	}
}