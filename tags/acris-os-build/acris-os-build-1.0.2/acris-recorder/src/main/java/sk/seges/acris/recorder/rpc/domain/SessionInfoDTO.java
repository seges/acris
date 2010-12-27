package sk.seges.acris.recorder.rpc.domain;

import sk.seges.acris.core.client.rpc.IDataTransferObject;

public class SessionInfoDTO implements IDataTransferObject {

	private static final long serialVersionUID = 3610456006317425801L;

	private String sessionId;
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	
}
