/**
 * 
 */
package sk.seges.acris.security.client.ui;

import com.google.gwt.user.client.ui.Composite;
import sk.seges.acris.security.client.ISecuredObject;
import sk.seges.acris.security.server.session.ClientSession;
import sk.seges.acris.security.shared.session.ClientSessionDTO;

/**
 * An acris-security enabled {@link Composite}.
 * 
 * @author ladislav.gazo
 */
public class SecuredComposite extends Composite implements ISecuredObject {

	private ClientSessionDTO clientSession;
	
	@Override
	public ClientSessionDTO getClientSession() {
		return clientSession;
	}

	@Override
	public void setClientSession(ClientSessionDTO clientSession) {
		this.clientSession = clientSession;
	}
}
