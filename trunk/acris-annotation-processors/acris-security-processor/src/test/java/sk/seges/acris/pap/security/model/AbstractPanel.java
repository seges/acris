package sk.seges.acris.pap.security.model;

import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;

public class AbstractPanel {

	private ClientSession<GenericUserDTO> clientSession;

	public void setClientSession(ClientSession<GenericUserDTO> clientSession) {
		this.clientSession = clientSession;
	}

	public ClientSession<GenericUserDTO> getClientSession() {
		return clientSession;
	}
}