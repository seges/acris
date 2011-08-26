/**
 * 
 */
package sk.seges.acris.core.showcase.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author ladislav.gazo
 */
//@RemoteServicePath("showcase-service/orderService")
@RemoteServiceRelativePath("showcase-service/orderService")
public interface OrderService extends RemoteService {
	void order(String name, double price);
}
