package sk.seges.acris.mvp.client.action.mocks;

import sk.seges.acris.mvp.shared.action.user.AddUserAction;
import sk.seges.acris.mvp.shared.result.user.AddUserResult;
import sk.seges.acris.showcase.client.action.mocks.core.MockActionHandler;

import com.google.inject.Inject;

public class MockAddUserActionHandler extends MockActionHandler<AddUserAction, AddUserResult> {

	@Inject
	public MockAddUserActionHandler() {	
	}
	
	@Override
	public AddUserResult execute(AddUserAction action) {
		return new AddUserResult(((AddUserAction) action).getUser());
	}

	@Override
	public Class<AddUserAction> getActionType() {
		return AddUserAction.class;
	}
}
