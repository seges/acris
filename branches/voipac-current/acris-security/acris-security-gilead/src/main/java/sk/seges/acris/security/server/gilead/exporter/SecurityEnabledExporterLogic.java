package sk.seges.acris.security.server.gilead.exporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.security.SpringSecurityException;

import sk.seges.acris.security.server.spring.exception.SecurityExceptionFactory;
import sk.seges.acris.security.shared.exception.ServerException;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;

/**
 * @author ladislav.gazo
 */
public class SecurityEnabledExporterLogic {
	protected static String handleInvocationTargetException(InvocationTargetException e, Object service, Method targetMethod,
			RPCRequest rpcRequest) throws Exception {
		Throwable cause = e.getCause();
		
		if (cause instanceof SpringSecurityException) {
			ServerException exception = SecurityExceptionFactory.get((SpringSecurityException)cause);
			return encodeResponseForFailure(rpcRequest, exception);
		}
		
		return null;
	}

	private static String encodeResponseForFailure(RPCRequest rpcRequest, Throwable cause) throws SerializationException{
		return RPC.encodeResponseForFailure(rpcRequest.getMethod(), cause, rpcRequest
				.getSerializationPolicy());
	}
}
