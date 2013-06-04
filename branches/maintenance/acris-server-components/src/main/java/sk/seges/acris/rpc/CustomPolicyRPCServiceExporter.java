package sk.seges.acris.rpc;

import javax.servlet.http.HttpServletRequest;

import org.gwtwidgets.server.spring.GWTRPCServiceExporter;

import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * Enhanced GWTRPCServiceExporter except it doesn't use default serialization
 * policy loading mechanism but custom serialization policy class.
 * 
 * @author eldzi
 */
public class CustomPolicyRPCServiceExporter extends GWTRPCServiceExporter implements ICustomSerializationPolicyServiceExporter {
	private static final long serialVersionUID = 794254705092767867L;
	private ICustomSerializationPolicy policy;

	@Override
	protected SerializationPolicy doGetSerializationPolicy(
			HttpServletRequest request, String moduleBaseURL, String strongName) {
		SerializationPolicy sPolicy = policy.doGetSerializationPolicy(getServletContext(), request, moduleBaseURL, strongName);
		return sPolicy;
	}

	@Override
	public void setSerializationPolicy(ICustomSerializationPolicy policy) {
		this.policy = policy;
	}
}
