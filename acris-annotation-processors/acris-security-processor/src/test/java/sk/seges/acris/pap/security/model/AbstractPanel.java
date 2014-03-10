package sk.seges.acris.pap.security.model;

import sk.seges.acris.security.shared.session.ClientSessionDTO;

public class AbstractPanel {

	private ClientSessionDTO clientSession;

	public void setClientSession(ClientSessionDTO clientSession) {
		this.clientSession = clientSession;
	}

	public ClientSessionDTO getClientSession() {
		return clientSession;
	}
}