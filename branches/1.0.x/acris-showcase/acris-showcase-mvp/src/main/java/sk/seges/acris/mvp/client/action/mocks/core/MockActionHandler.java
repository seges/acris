package sk.seges.acris.mvp.client.action.mocks.core;

import sk.seges.acris.mvp.client.action.mocks.registrator.MockRegistrator;

import com.philbeaudoin.gwtp.dispatch.shared.Action;
import com.philbeaudoin.gwtp.dispatch.shared.Result;

public abstract class MockActionHandler<A extends Action<R>, R extends Result> {

	protected MockActionHandler() {
		MockRegistrator.put(getActionType(), this);
	}
	
	public abstract R execute(A action);

    protected abstract Class<A> getActionType();
}