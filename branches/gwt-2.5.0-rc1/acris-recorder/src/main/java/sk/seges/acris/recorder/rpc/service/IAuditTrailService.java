package sk.seges.acris.recorder.rpc.service;

import sk.seges.acris.recorder.rpc.transfer.StringMapper;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Audit trail is a chronological sequence of audit records, each of which
 * contains evidence user activity. We are interesting of: 
 * - mouse events (mouse click, mouse over, mouse leave, ...) 
 * - keyboard events (keydown, keypress, keyup) 
 * - html events (blur, focus, contextmenu, change, ...)
 * 
 * @author fat
 * 
 */
public interface IAuditTrailService extends RemoteService {
	
	public int[] getAuditLogs(StringMapper mapper);
	
	/**
	 * Logging event of current user. User is stored in client context and is identified
	 * via sessionID on the server side. SessionID is bundled in the payload - see security
	 * implementation for the details.
	 * 
	 * @param event
	 *            is represented as a number in order to have the highest
	 *            compression as possible
	 *            Some events are encoded into more than 1 integer - then use second version
	 *            of the method
	 * @param targetId
	 * 			  represents element ID of the target HTML element. 
	 * Important!! In case of events that doesn't have targetID use another method
	 * without targetId signature!!
	 * @param deltaTime
	 * 			  relative time when event was recorded
	 * Important!! In case of events when you don't want to log time use another method
	 * without deltaTime signature
	 */
	public void logUserActivity(int event, String targetId);
	public void logUserActivity(int[] event, String targetId);
	public void logUserActivity(int[] event, int deltaTime, String targetId);
	public void logUserActivity(int[] event, int[] deltaTimes, String targetId);

	/**
	 * Logging events (without targetId) of current user - event doesn't have 
	 * targetId so the serialized payload has smaller size then in method with targetId 
	 * signature
	 * 
	 * @param event
	 *            is represented as a number in order to have the highest
	 *            compression as possible
	 *            Some events are encoded into more than 1 integer - then use second version
	 *            of the method
	 */
	public void logUserActivity(int event);
	public void logUserActivity(int[] event);
	public void logUserActivity(int event, int deltaTime);
	public void logUserActivity(int[] event, int deltaTime);

	/**
	 * Logging list of user events. Events are associated it the user, which is stored in
	 * client context and is identified via sessionID on the server side. SessionID is
	 * bundled in the payload - see security implementation for the details
	 * @param events
	 * 				list of events. Each event is represented as a number (or more numbers) 
	 * 				in order to have the highest compression as possible.
	 * @param targetIds
	 * 				list of the event target identifiers. Many times are events related to the 
	 * 				same target elements or they are without targets (Html events, some mouse 
	 * 				events, ...), so it is better to handle them in the specific list and not
	 * 				bundle them into the compressed event representation
	 */
	public void logUserActivity(int[] events, String[] targetIds);
	public void logUserActivity(int[] events, int[] deltaTimes, String[] targetIds);

}