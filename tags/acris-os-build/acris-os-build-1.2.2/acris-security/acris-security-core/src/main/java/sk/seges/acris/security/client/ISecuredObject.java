package sk.seges.acris.security.client;

import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;

/**
 * General secured object applied on client side. Mostly is is used on GWT
 * panels and GWT UI components. Client authorities are taken from
 * {@link GenericUserDTO} which is stored in {@link ClientSession}, so thats the
 * reason why you have to set {@link ClientSession} to the secured object.
 * 
 * @author fat
 * 
 */
public interface ISecuredObject {
	void setClientSession(ClientSession clientSession);

	ClientSession getClientSession();
}
