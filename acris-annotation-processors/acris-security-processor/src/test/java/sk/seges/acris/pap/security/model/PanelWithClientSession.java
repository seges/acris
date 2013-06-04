package sk.seges.acris.pap.security.model;

import sk.seges.acris.security.client.annotations.Secured;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;

import com.google.gwt.user.client.ui.Label;

@Secured
public class PanelWithClientSession  {

	@Secured("TEXT")
	Label label1;

	private ClientSession<GenericUserDTO> clientSession;

	public void setClientSession(ClientSession<GenericUserDTO> clientSession) {
		this.clientSession = clientSession;
	}

	public ClientSession<GenericUserDTO> getClientSession() {
		return clientSession;
	}

}