package sk.seges.acris.mvp.client.action.mocks.manager;

import sk.seges.acris.mvp.client.action.ActionManager;
import sk.seges.acris.mvp.client.action.DefaultAsyncCallback;
import sk.seges.acris.mvp.client.action.mocks.core.MockActionHandler;
import sk.seges.acris.mvp.client.action.mocks.registrator.MockRegistrator;
import sk.seges.acris.mvp.client.presenter.core.ErrorPresenter;

import com.google.inject.Inject;
import com.philbeaudoin.gwtp.dispatch.client.DispatchAsync;
import com.philbeaudoin.gwtp.dispatch.shared.Action;
import com.philbeaudoin.gwtp.dispatch.shared.Result;

public class MockActionManager extends ActionManager {

	@Inject
	public MockActionManager(DispatchAsync dispatcher, ErrorPresenter errorPresenter) {
		super(dispatcher, errorPresenter);
	}
	
//	@SuppressWarnings("unchecked")
//	protected <A extends Action<R>, R extends Result> MockActionHandler<A, R> getActionHandler(A action) {
//		if (action instanceof AddUserAction) {
//			return (MockActionHandler<A, R>) new MockAddUserActionHandler();
//		}
//		
//		if (action instanceof FetchUsersAction) {
//			return (MockActionHandler<A, R>) new MockAddUserActionHandler();
//		}
//
//		return null;
//	}
//	
	@Override
	public <A extends Action<R>, R extends Result> void execute(A action, final DefaultAsyncCallback<R> callback) {
		@SuppressWarnings("unchecked")
		MockActionHandler<A, R> actionHandler = (MockActionHandler<A, R>) MockRegistrator.get((Class<? extends Action<?>>) action.getClass());
		
		if (actionHandler != null) {
			callback.onFailure(new RuntimeException("Unhandled action"));
		}

		callback.onSuccess(actionHandler.execute(action));
	}
}
