/**
 * 
 */
package sk.seges.acris.callbacks.client;

import java.util.Date;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Extended interface for {@link AsyncCallback} for providing additional
 * tracking information of the request.
 * <p>
 * Used to pair parent requests and child requests - requests created in
 * callback processing routines (onSuccess and onFailure) are called child
 * request and are directly associated with parent requests. Parent requests are
 * finished only when all child requests are also finished - response was
 * received and callback processed.
 * </p>
 * 
 * @author fat
 * 
 * @param <T>
 *            The type of the return value
 */
public abstract class TrackingAsyncCallback<T> implements AsyncCallback<T> {

	/**
	 * Request associated with TrackingAsyncCallback
	 */
	private RPCRequest request;

	public TrackingAsyncCallback() {
	}

	public void setName(String name) {
		if (request == null) {
			return;
		}
		request.setName(name);
	}
	
	/**
	 * When request is created in SecureRemoteServiceProxy class, then unique
	 * requestId is created and set to the TrackingAsyncCallback. It means that
	 * it was created, send to the server and waiting for the response.
	 * 
	 * @param requestId
	 */
	public void setRequestId(long requestId) {
		request = new RPCRequest(requestId);

		// Getting currently processed request. Currently process request (if
		// exists) will be parent request and parent request will be finished
		// only when all his child requests are finished
		RPCRequest parentRequest = RPCRequestTracker.getTracker()
				.getProcessingRequest();

		if (parentRequest != null) {
			request.setParentRequest(parentRequest);
			parentRequest.addChildRequest(request);
		}

		request.setCallbackState(CallbackState.REQUEST_STARTED);

		RPCRequestTracker.getTracker().onRequestStarted(request);
	}

	/**
	 * Handling received response, setting up the correct state and notifying
	 * all callbacks
	 */
	private void handleResponseReceived() {
		RPCRequestTracker.getTracker().setProcessingRequest(request);
		request.setCallbackState(CallbackState.RESPONSE_RECEIVED);

		RPCRequestTracker.getTracker().onResponseReceived(request);
	}

	/**
	 * Setting up the correct state after all processing is finished and
	 * notifying all callbacks
	 */
	private void handleResponseFinished() {
		request.setCallbackState(CallbackState.RESPONSE_FINISHED);

		RPCRequestTracker.getTracker().onProcessingFinished(request);

		// callback processing is finished. We do need the request anymore
		this.request = null;
	}

	/**
	 * Called when an asynchronous call fails to complete normally.
	 * 
	 * @param caught
	 *            failure encountered while executing a remote procedure call
	 */
	public final void onFailure(Throwable caught) {
		if (request != null) {
			request.setCallbackResult(RequestState.REQUEST_FAILURE);
			request.setCaught(caught);
			this.handleResponseReceived();
		}

		try {
			onFailureCallback(caught);
		} finally {
			if (request != null) {
				this.handleResponseFinished();
			}
		}
	}

	/**
	 * Called when an asynchronous call completes successfully. All tracking
	 * states are stored and flow is handled to onSuccessCallback method
	 * 
	 * @param result
	 *            the return value of the remote produced call
	 */
	public final void onSuccess(T result) {

		if (request != null) {
			request.setCallbackResult(RequestState.REQUEST_SUCCESS);
			this.handleResponseReceived();
		}

		try {
			onSuccessCallback(result);
		} finally {
			if (request != null) {
				this.handleResponseFinished();
			}
		}
	}

	/**
	 * Emulates that async callback is started and we'll wait until it's finished.
	 * Used in async cases that are not related to RPC, but it is required to wait
	 * until it's finished
	 */
	public TrackingAsyncCallback<T> start(String name) {
		setRequestId(new Date().getTime());
		if (name == null) {
			setName("" + request.getRequestId());
		} else {
			setName(name);
		}
		return this;
	}
	
	/**
	 * Called when an asynchronous call completes successfully.
	 */
	public abstract void onSuccessCallback(T arg0);

	/**
	 * Called when an asynchronous call fails to complete normally.
	 * 
	 * @param caught
	 *            failure encountered while executing a remote procedure call
	 */
	public abstract void onFailureCallback(Throwable cause);
}