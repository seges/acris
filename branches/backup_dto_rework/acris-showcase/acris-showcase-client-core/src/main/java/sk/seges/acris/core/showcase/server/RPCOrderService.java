/**
 * 
 */
package sk.seges.acris.core.showcase.server;

import org.springframework.stereotype.Service;

import sk.seges.acris.core.showcase.shared.OrderService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ladislav.gazo
 */
@Service
public class RPCOrderService extends RemoteServiceServlet implements OrderService {
	private static final long serialVersionUID = 5437893262356874008L;

	@Override
	public void order(String name, double price) {
		try {
			Thread.sleep(Double.valueOf(price * 1000).longValue());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		if(price > 5.0) {
			throw new RuntimeException("Resistance is useless");
		}
	}

}
