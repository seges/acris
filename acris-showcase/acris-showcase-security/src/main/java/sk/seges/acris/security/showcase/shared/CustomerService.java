/**
 * 
 */
package sk.seges.acris.security.showcase.shared;

import sk.seges.acris.security.rpc.exception.ServerException;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author ladislav.gazo
 */
public interface CustomerService extends RemoteService {
	String getCustomerName() throws ServerException;
}
