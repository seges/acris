package sk.seges.acris.rpc.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SessionInfoDTO implements IsSerializable {

	private static final long serialVersionUID = 3610456006317425801L;

	private String sessionId;
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	
}
