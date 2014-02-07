/**
 * 
 */
package sk.seges.acris.callbacks.client;

import sk.seges.sesam.shared.model.dao.ICallback;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Adapter between SeSAM {@link ICallback} and GWT's {@link AsyncCallback}.
 * 
 * @author ladislav.gazo
 */
public class CallbackAdapter<T> extends TrackingAsyncCallback<T> {
	private final ICallback<T> adaptee;
	
	public CallbackAdapter(ICallback<T> adaptee) {
		this.adaptee = adaptee;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
	 */
	public void onFailureCallback(Throwable arg0) {
		adaptee.onFailure(arg0);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
	 */
	public void onSuccessCallback(T arg0) {
		adaptee.onSuccess(arg0);
	}
}
