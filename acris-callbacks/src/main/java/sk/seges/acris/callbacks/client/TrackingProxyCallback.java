package sk.seges.acris.callbacks.client;

import java.util.Date;

import com.google.gwt.user.client.AsyncProxy.ProxyCallback;

@SuppressWarnings("deprecation")
public abstract class TrackingProxyCallback<T> extends ProxyCallback<T> {

	private TrackingAsyncCallback<T> callback;
	
	protected TrackingProxyCallback() {
		callback = new TrackingAsyncCallback<T>() {

			@Override
			public void onSuccessCallback(T arg0) {
				TrackingProxyCallback.this.onSuccessCallback(arg0);
			}

			@Override
			public void onFailureCallback(Throwable cause) {
				TrackingProxyCallback.this.onFailureCallback(cause);
			}
		};
		
		long requestId = new Date().getTime();

		callback.setRequestId(requestId);
		callback.setName("" + requestId);
	}
	
	public final void onComplete(T instance) {
		callback.onSuccess(instance);
	}

	public final void onFailure(Throwable t) {
		callback.onFailure(t);
	}

	public void onSuccessCallback(T arg0) {}

	public void onFailureCallback(Throwable cause) {}

}