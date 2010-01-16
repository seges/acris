package sk.seges.acris.callbacks.client;

/**
 * Listener for handling {@link TrackingAsyncCallback} state changes. 
 * 3 types of state change can occur:
 * <ul>
 * <li>Request started - when request is created, is sent to the server and is waiting for the response</li>
 * <li>Response received - when response is received and callback processing started</li>
 * <li>Processing finished - when all callback processing routines finished</li>
 * </ul> 
 * 
 * @author fat
 */
public interface ICallbackTrackingListener {

	void onRequestStarted(RPCRequest request);

	void onResponseReceived(RPCRequest request);

	void onProcessingFinished(RPCRequest request);
}