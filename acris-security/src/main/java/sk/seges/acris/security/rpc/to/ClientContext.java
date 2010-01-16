package sk.seges.acris.security.rpc.to;

import sk.seges.acris.security.rpc.domain.GenericUser;
import sk.seges.acris.security.rpc.domain.ITransferableObject;

public class ClientContext implements ITransferableObject {

	private static final long serialVersionUID = -4010165366508070676L;
	
	private GenericUser user;
	private String sessionId;

	public ClientContext() {
	}

	public GenericUser getUser() {
		return user;
	}

	public void setUser(GenericUser user) {
		this.user = user;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
