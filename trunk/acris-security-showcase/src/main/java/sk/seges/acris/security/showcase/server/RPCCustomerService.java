/**
 * 
 */
package sk.seges.acris.security.showcase.server;

import org.springframework.stereotype.Service;

import sk.seges.acris.security.showcase.shared.CustomerService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author ladislav.gazo
 */
@Service
public class RPCCustomerService extends RemoteServiceServlet implements CustomerService {
	private static final long serialVersionUID = 5437893262356874008L;

	@Override
	public String getCustomerName() {
		return "AcrIS big brother";
	}
}
