package sk.seges.acris.security.server.exporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sk.seges.acris.rpc.CustomPolicyRPCServiceExporter;

import com.google.gwt.user.server.rpc.RPCRequest;

public class SecurityEnabledCustomServiceExporter extends CustomPolicyRPCServiceExporter {
	private static final long serialVersionUID = -7035893923368347819L;

	@Override
	protected String handleInvocationTargetException(InvocationTargetException e, Object service, Method targetMethod,
			RPCRequest rpcRequest) throws Exception {
		String delegated = SecurityEnabledExporterLogic.handleInvocationTargetException(e, service, targetMethod, rpcRequest);
		return (delegated != null ? delegated : super.handleInvocationTargetException(e, service, targetMethod, rpcRequest));
	}
}
