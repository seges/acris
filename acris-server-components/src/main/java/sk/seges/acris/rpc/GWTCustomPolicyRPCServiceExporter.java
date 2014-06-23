package sk.seges.acris.rpc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.ServletContextAware;

import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * Enhanced GWTRPCServiceExporter except it doesn't use default serialization
 * policy loading mechanism but custom serialization policy class. It is also able to use Hibernate4GWT specific logic in RPC calls.
 *
 * @author eldzi
 * @author mig
 */
public class GWTCustomPolicyRPCServiceExporter extends GWTRPCServiceExporter implements ICustomSerializationPolicyServiceExporter, ServletContextAware {
	private static final long serialVersionUID = 888485619457236610L;
	private ICustomSerializationPolicy policy;

	@Override
	protected SerializationPolicy doGetSerializationPolicy(
			HttpServletRequest request, String moduleBaseURL, String strongName) {
	    return policy.doGetSerializationPolicy(getServletContext(), request, moduleBaseURL, strongName);
	}

	@Override
	public void setSerializationPolicy(ICustomSerializationPolicy policy) {
		this.policy = policy;
	}
}
