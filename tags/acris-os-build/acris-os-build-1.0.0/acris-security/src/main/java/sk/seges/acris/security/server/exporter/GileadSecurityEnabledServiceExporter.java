package sk.seges.acris.security.server.exporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.gwtwidgets.server.spring.gilead.GileadRPCServiceExporter;

import com.google.gwt.user.server.rpc.RPCRequest;

public class GileadSecurityEnabledServiceExporter extends GileadRPCServiceExporter {
	private static final long serialVersionUID = -1298425847249451171L;

	@Override
	protected String handleInvocationTargetException(InvocationTargetException e, Object service, Method targetMethod,
			RPCRequest rpcRequest) throws Exception {
		String delegated = SecurityEnabledExporterLogic.handleInvocationTargetException(e, service, targetMethod, rpcRequest);
		return (delegated != null ? delegated : super.handleInvocationTargetException(e, service, targetMethod, rpcRequest));
	}
}
