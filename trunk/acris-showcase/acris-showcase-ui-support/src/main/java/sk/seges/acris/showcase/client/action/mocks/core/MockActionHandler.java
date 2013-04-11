package sk.seges.acris.showcase.client.action.mocks.core;

import sk.seges.acris.showcase.client.action.mocks.registrator.MockRegistrator;

import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.Result;

public abstract class MockActionHandler<A extends Action<R>, R extends Result> {

	protected MockActionHandler() {
		MockRegistrator.put(getActionType(), this);
	}
	
	public abstract R execute(A action);

    protected abstract Class<A> getActionType();
}