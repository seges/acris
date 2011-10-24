/**
 * 
 */
package sk.seges.acris.security.showcase.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ladislav.gazo
 */
public interface CustomerServiceAsync {
	void getCustomerName(AsyncCallback<String> callback);
}
