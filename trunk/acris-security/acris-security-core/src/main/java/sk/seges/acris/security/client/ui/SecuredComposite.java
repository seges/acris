/**
 * 
 */
package sk.seges.acris.security.client.ui;

import sk.seges.acris.security.client.ISecuredObject;
import sk.seges.acris.security.shared.session.ClientSession;

import com.google.gwt.user.client.ui.Composite;

/**
 * An acris-security enabled {@link Composite}.
 * 
 * @author ladislav.gazo
 */
public class SecuredComposite extends Composite implements ISecuredObject {
	private ClientSession clientSession;
	
	@Override
	public ClientSession getClientSession() {
		return clientSession;
	}

	@Override
	public void setClientSession(ClientSession clientSession) {
		this.clientSession = clientSession;
	}
}
