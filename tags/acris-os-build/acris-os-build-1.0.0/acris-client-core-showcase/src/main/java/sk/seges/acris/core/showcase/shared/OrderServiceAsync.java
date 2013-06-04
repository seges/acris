/**
 * 
 */
package sk.seges.acris.core.showcase.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ladislav.gazo
 */
public interface OrderServiceAsync {
	void order(String name, double price, AsyncCallback<Void> callback);
}
