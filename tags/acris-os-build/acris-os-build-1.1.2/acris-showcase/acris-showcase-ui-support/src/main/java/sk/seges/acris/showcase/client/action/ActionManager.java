package sk.seges.acris.showcase.client.action;

import sk.seges.acris.showcase.client.presenter.FailureHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.Result;

public class ActionManager {

	private final DispatchAsync dispatcher;
	private final FailureHandler errorPresenter;

	@Inject
	public ActionManager(DispatchAsync dispatcher, FailureHandler errorPresenter) {
		this.dispatcher = dispatcher;
		this.errorPresenter = errorPresenter;
	}

	public <A extends Action<R>, R extends Result> void execute(A action, final DefaultAsyncCallback<R> callback) {
		dispatcher.execute(action, new AsyncCallback<R>() {

			@Override
			public void onFailure(Throwable caught) {
				if (callback.handleFailures()) {
					callback.onFailure(caught);
				} else {
					errorPresenter.handleFailure(caught);
				}
			}

			@Override
			public void onSuccess(R arg0) {
				callback.onSuccess(arg0);
			}
		});
	}
}