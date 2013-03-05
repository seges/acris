package sk.seges.acris.showcase.client.action.mocks.manager;

import sk.seges.acris.showcase.client.action.ActionManager;
import sk.seges.acris.showcase.client.action.DefaultAsyncCallback;
import sk.seges.acris.showcase.client.action.mocks.core.MockActionHandler;
import sk.seges.acris.showcase.client.action.mocks.registrator.MockRegistrator;
import sk.seges.acris.showcase.client.presenter.FailureHandler;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.Result;

public class MockActionManager extends ActionManager {

	@Inject
	public MockActionManager(DispatchAsync dispatcher, FailureHandler failureHandler) {
		super(dispatcher, failureHandler);
	}
	
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
