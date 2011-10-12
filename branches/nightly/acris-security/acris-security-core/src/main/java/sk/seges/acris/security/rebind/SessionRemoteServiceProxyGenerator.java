package sk.seges.acris.security.rebind;

import sk.seges.acris.core.client.annotation.RemoteServicePath;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.rpc.ProxyCreator;
import com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator;
import com.google.gwt.user.rebind.rpc.SessionProxyCreator;

/**
 * Generator used to create:
 * <ul>
 * <li>Session aware remote proxy service</li>
 * <li>Session proxy automatically initialized using {@link RemoteServicePath} annotation</li>
 * </ul>
 * @author fat
 *
 */
public class SessionRemoteServiceProxyGenerator extends ServiceInterfaceProxyGenerator {
	@Override
	protected ProxyCreator createProxyCreator(JClassType remoteService) {
		return new SessionProxyCreator(remoteService);
	}
}