package sk.seges.acris.security.server.service;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class GuiceRemoteServiceServlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 1338141601638421382L;

	@Inject
	private Injector injector;

	@Override
	public String processCall(String payload) throws SerializationException {
		try {
			RPCRequest req = RPC.decodeRequest(payload, null, this);
			RemoteService service = getServiceInstance(req.getMethod().getDeclaringClass());

			return RPC.invokeAndEncodeResponse(service, req.getMethod(), req.getParameters(),
					req.getSerializationPolicy(), req.getFlags());
		} catch (IncompatibleRemoteServiceException ex) {
			log("IncompatibleRemoteServiceException in the processCall(String) method.", ex);
			return RPC.encodeResponseForFailure(null, ex);
		}
	}

	private RemoteService getServiceInstance(Class<?> serviceClass) {
		return (RemoteService) injector.getInstance(serviceClass);
	}
}
