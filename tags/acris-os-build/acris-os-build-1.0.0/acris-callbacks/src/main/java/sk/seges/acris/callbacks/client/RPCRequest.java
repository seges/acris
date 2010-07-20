package sk.seges.acris.callbacks.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RPCRequest {
	private long requestId;

	private RPCRequest parentRequest;
	private CallbackState callbackState;
	private RequestState callbackResult;

	private List<RPCRequest> childReqests = new ArrayList<RPCRequest>(3);

	private Throwable caught;
	
	private Map<CallbackState, Date> callbackStateTimes = new HashMap<CallbackState, Date>(5);

	public RPCRequest(long reuqestId) {
		this.requestId = reuqestId;
	}

	public RequestState getCallbackResult() {
		return callbackResult;
	}

	public void setCallbackResult(RequestState callbackResult) {
		this.callbackResult = callbackResult;
	}

	public CallbackState getCallbackState() {
		return callbackState;
	}

	public Date getCallbackStateTime(CallbackState callbackState) {
		return callbackStateTimes.get(callbackState);
	}
	
	public void setCallbackState(CallbackState callbackState) {
		this.callbackState = callbackState;

		Date currentDate = new Date(System.currentTimeMillis());
		callbackStateTimes.put(callbackState, currentDate);
	}

	public long getRequestId() {
		return this.requestId;
	}

	public boolean addChildRequest(RPCRequest childRequest) {
		return childReqests.add(childRequest);
	}

	public boolean removeChildRequest(RPCRequest childRequest) {
		return childReqests.remove(childRequest);
	}

	public boolean hasChildrens() {
		return this.childReqests.size() > 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RPCRequest) {
			return this.requestId == ((RPCRequest) obj).requestId;
		}
		return false;
	}

	public boolean equals(long requestId) {
		return this.requestId == requestId;
	}

	@Override
	public int hashCode() {
		//TODO Think about this
		return (int) this.requestId;
	}

	@Override
	public String toString() {
		return " Id is : " + this.requestId;
	}

	public RPCRequest getParentRequest() {
		return parentRequest;
	}

	public void setParentRequest(RPCRequest parentRequest) {
		this.parentRequest = parentRequest;
	}

	public Throwable getCaught() {
		return caught;
	}

	public void setCaught(Throwable caught) {
		this.caught = caught;
	}
}