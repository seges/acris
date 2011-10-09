package sk.seges.acris.mvp.client.action.mocks;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.mvp.client.action.mocks.factory.UsersFactory;
import sk.seges.acris.mvp.shared.action.user.FetchUsersAction;
import sk.seges.acris.mvp.shared.result.user.FetchUsersResult;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.showcase.client.action.mocks.core.MockActionHandler;
import sk.seges.sesam.dao.PagedResult;

import com.google.inject.Inject;

public class MockFetchUsersActionHandler extends MockActionHandler<FetchUsersAction, FetchUsersResult> {

	@Inject
	public MockFetchUsersActionHandler() {
	}
	
	@Override
	public FetchUsersResult execute(FetchUsersAction action) {
		UserData<?> user = UsersFactory.createMockUser(1L, "test", "test");

		PagedResult<List<UserData<?>>> pagedUsers = new PagedResult<List<UserData<?>>>();
		List<UserData<?>> users = new ArrayList<UserData<?>>();
		users.add(user);
		pagedUsers.setResult(users);

		return new FetchUsersResult(pagedUsers);
	}

	@Override
	public Class<FetchUsersAction> getActionType() {
		return FetchUsersAction.class;
	}
}