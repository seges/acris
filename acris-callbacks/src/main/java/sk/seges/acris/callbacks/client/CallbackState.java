package sk.seges.acris.callbacks.client;

/**
 * State of the {@link TrackingAsyncCallback}.
 * 3 types of state change can occur:
 * <ul>
 * <li>Request started - when request is created, is sent to the server and is waiting for the response</li>
 * <li>Response received - when response is received and callback processing started</li>
 * <li>Processing finished - when all callback processing routines finished</li>
 * </ul> 
 * 
 * @author fat
 */
public enum CallbackState {
	/**
	 * An asynchronous request is started and sent to the server
	 */
	REQUEST_STARTED,
	/**
	 * Request response is received and callback method are processed
	 */
	RESPONSE_RECEIVED,
	/**
	 * Callback methods are executed and request is finished and processed
	 */
	RESPONSE_FINISHED;
}
