/**
 * 
 */
package sk.seges.acris.callbacks.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to tracking all RPC requests. RPCRequestTracker can be used to:
 * <ul>
 * <li>Determine how much requests is currently waiting for the response</li>
 * <li>Determine which request callback is currently processed</li>
 * <li>For notifying when asynchronous RPC requests is finished (also with all
 * his subrequests)</li>
 * </ul>
 * 
 * @author fat
 */
public class RPCRequestTracker {

	/**
	 * All registered listeners for notifying when request/callback state is
	 * changed
	 */
	private List<ICallbackTrackingListener> callbackListeners = new ArrayList<ICallbackTrackingListener>();

	/**
	 * Singleton instance for the tracker
	 */
	final private static RPCRequestTracker instance = new RPCRequestTracker();
	private static final List<RPCRequest> requests = new ArrayList<RPCRequest>();

	/**
	 * Number of current running requests (number of requests in state
	 * CallbackState.REQUEST_STARTED)
	 */
	private static int runningRequestStarted = 0;

	/**
	 * Current processed request - due to the single threaded JS design is only
	 * one RPC request processed in time
	 */
	private List<RPCRequest> processingRequests = new ArrayList<RPCRequest>();

	RPCRequest getProcessingRequest() {
		if (processingRequests.size() > 0) {
			return processingRequests.get(processingRequests.size() - 1);
		}
		
		return null;
	}

	void setProcessingRequest(RPCRequest processingRequest) {
		this.processingRequests.add(processingRequest);
	}

	/**
	 * Method for RPC request cleanup. Used when all processing is finished.
	 * Because of single-threaded JS nature only one request can be processed in
	 * the time. When new request is started/created, this processed request
	 * will be his parent request (it means - new request was created from
	 * callback onSuccess/onFailure handlers and it is considered as child
	 * request)
	 * 
	 * @param processingRequest
	 */
	void resetProcessingRequest(RPCRequest processingRequest) {
//		if (!processingRequest.equals(this.getProcessingRequest())) {
//			throw new IllegalArgumentException(
//					"Processing 2 parallel requests?. That's impossible in JS. Check the bugs, man.");
//		}
		this.processingRequests.remove(processingRequests.size() - 1);
	}

	/**
	 * Only singleton instance exists. Instance can be obtained via getTracker
	 * method
	 */
	private RPCRequestTracker() {
	}

	/**
	 * @return singleton instance
	 */
	public static RPCRequestTracker getTracker() {
		return instance;

	}

	/**
	 * Register new {@link ICallbackTrackingListener} for tracking request
	 * state.
	 * 
	 * @param callbackTrackingListener
	 *            listener will be notified when any RPC request changed his
	 *            state
	 */
	public void registerCallbackListener(
			ICallbackTrackingListener callbackTrackingListener) {
		callbackListeners.add(callbackTrackingListener);
	}

	/**
	 * Removes first occurrence of the registered listener
	 * 
	 * @param callbackTrackingListener
	 *            to be removed
	 * @return true if the listener was removed
	 */
	public boolean removeCallbackListener(
			ICallbackTrackingListener callbackTrackingListener) {
		return callbackListeners.remove(callbackTrackingListener);
	}

	/**
	 * Removes all registered listeners
	 */
	public void removeAllCallbacks() {
		callbackListeners.clear();
	}

	/**
	 * Method for handling RPC request starting state. RPC request is in started
	 * state when is constructed, sent to the server and waiting for the
	 * response
	 * 
	 * @param request
	 *            with changed state
	 * 
	 */
	void onRequestStarted(RPCRequest request) {
		runningRequestStarted++;
		requests.add(request);
		
		ICallbackTrackingListener[] listeners = callbackListeners.toArray(new ICallbackTrackingListener[0]);

		for (ICallbackTrackingListener callbackListener : listeners) {
			callbackListener.onRequestStarted(request);
		}
	}

	/**
	 * Method for handling RPC request received state. When server sent response
	 * to the RPC request then his state is changed into
	 * CallbackState.RESPONSE_RECEIVED and response processing is started. 2
	 * possibilities exists:
	 * <ul>
	 * <li>RPC request failed - RPC Request is set to
	 * RequestState.REQUEST_FAILURE</li>
	 * <li>RPC request success - RPC Request is set to
	 * RequestState.REQUEST_SUCCESS</li>
	 * </ul>
	 * When RPC Request response is received, and callback for this request
	 * starts new RPC request, then new request is considered as child RPC
	 * request and original RPC request is considered as finished when all his
	 * child RPC requests are finished.
	 * 
	 * @param request
	 *            with changed state
	 * 
	 */
	void onResponseReceived(RPCRequest request) {
		runningRequestStarted--;
		requests.remove(request);
		
		ICallbackTrackingListener[] listeners = callbackListeners.toArray(new ICallbackTrackingListener[0]);

		for (ICallbackTrackingListener callbackListener : listeners) {
			callbackListener.onResponseReceived(request);
		}
	}

	/**
	 * When RPC request callback processing is finished, this method is
	 * executed. The main purpose is to clean-up all necessary stuff and call
	 * registered listeners.
	 * 
	 * When RPC Request response is received, and callback for this request
	 * starts new RPC request, then new request is considered as child RPC
	 * request and original RPC request is considered as finished when all his
	 * child RPC requests are finished.
	 * 
	 * @param request
	 *            with changed state
	 * @return true if request was removed otherwise false
	 */
	boolean onProcessingFinished(RPCRequest request) {

		this.resetProcessingRequest(request);

		return finishedRequest(request);
	}

	/**
	 * RPC request is considered as finished only when all his child requests
	 * are finished. Method is responsible for notifying registered callbacks
	 * and for correct cleanup of parent request.
	 * 
	 * @param request
	 *            with finished processing routines
	 * 
	 * @return true if request was removed otherwise false
	 */
	private boolean finishedRequest(RPCRequest request) {

		if (request.hasChildrens()) {
			// waiting for finish all childrens
			return false;
		}

		if (!request.getCallbackState().equals(CallbackState.RESPONSE_FINISHED)) {
			return false;
		}

		ICallbackTrackingListener[] listeners = callbackListeners.toArray(new ICallbackTrackingListener[0]);
		
		for (ICallbackTrackingListener callbackListener : listeners) {
			callbackListener.onProcessingFinished(request);
		}

		if (request.getParentRequest() == null) {
			return true;
		}

		request.getParentRequest().removeChildRequest(request);

		finishedRequest(request.getParentRequest());

		return true;
	}

	/**
	 * Method used to determine number of running RPC request. Running RPC
	 * request means that it was started but response did not received yet.
	 * 
	 * @return number of running RPC requests
	 */
	public static int getRunningRequestStarted() {
		return runningRequestStarted;
	}
	
	public static List<RPCRequest> getAwaitingRequests() {
		return requests;
	}
}