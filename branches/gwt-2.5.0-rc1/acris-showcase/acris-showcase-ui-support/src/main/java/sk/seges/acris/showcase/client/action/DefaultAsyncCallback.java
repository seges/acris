package sk.seges.acris.showcase.client.action;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class DefaultAsyncCallback<T> implements AsyncCallback<T> {

	public boolean handleFailures() {
		return false;
	}

	@Override
	public void onFailure(Throwable arg0) {

	}
}
