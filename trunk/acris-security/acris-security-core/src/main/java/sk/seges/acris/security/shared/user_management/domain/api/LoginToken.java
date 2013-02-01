/**
 * 
 */
package sk.seges.acris.security.shared.user_management.domain.api;

import sk.seges.acris.core.client.rpc.IDataTransferObject;

/**
 * A token transferring login information to a user service (or user service
 * broadcaster). The token might be specific for a service but when used in
 * conjunction with service broadcaster it must hold login information common to
 * all user services.
 * 
 * There is a transformation executed in the service layer where the login token
 * is transformed to authentication token used on the server side.
 * 
 * @author ladislav.gazo
 */
public interface LoginToken extends IDataTransferObject {
	
	String getWebId();
}
