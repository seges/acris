package sk.seges.acris.security.client;

import sk.seges.acris.security.rpc.session.ClientSession;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;

/**
 * General secured object applied on client side. Mostly is is used on GWT
 * panels and GWT UI components. Client authorities are taken from
 * {@link GenericUser} which is stored in {@link ClientSession}, so thats the
 * reason why you have to set {@link ClientSession} to the secured object.
 * 
 * @author fat
 * 
 */
public interface ISecuredObject {
	void setClientSession(ClientSession clientSession);

	ClientSession getClientSession();
}
