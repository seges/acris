package sk.seges.acris.mvp.client.action.mocks;

import com.google.inject.Inject;

import sk.seges.acris.mvp.client.action.mocks.core.MockActionHandler;
import sk.seges.acris.mvp.shared.action.user.AddUserAction;
import sk.seges.acris.mvp.shared.result.user.AddUserResult;

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
