package sk.seges.acris.pap.security.model;

import com.google.gwt.user.client.ui.Label;
import sk.seges.acris.security.client.annotations.Secured;
import sk.seges.acris.security.shared.session.ClientSessionDTO;

@Secured
public class PanelWithClientSession  {

	@Secured("TEXT")
	Label label1;

	private ClientSessionDTO clientSession;

	public void setClientSession(ClientSessionDTO clientSession) {
		this.clientSession = clientSession;
	}

	public ClientSessionDTO getClientSession() {
		return clientSession;
	}

}