package sk.seges.acris.security.rebind;

import sk.seges.acris.core.client.annotation.RemoteServicePath;
import sk.seges.acris.core.rebind.RemoteServiceProviderCreator;
import sk.seges.acris.core.rebind.RemoteServiceWrapperCreator;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
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
public class SessionRemoteServiceProxyGenerator extends Generator {

	private static final String QUOTE = "'";

	public SessionRemoteServiceProxyGenerator() {
	}
	
	@Override
	public String generate(TreeLogger logger, GeneratorContext ctx,
			String requestedClass) throws UnableToCompleteException {
		
		final TypeOracle typeOracle = ctx.getTypeOracle();
		assert typeOracle != null;

		final JClassType remoteService = typeOracle.findType(requestedClass);
		if (remoteService == null) {
			logger.log(TreeLogger.ERROR, "Unable to find metadata for type '"
					+ requestedClass + QUOTE, null);
			throw new UnableToCompleteException();
		}

		if (remoteService.isInterface() == null) {
			logger.log(TreeLogger.ERROR, remoteService.getQualifiedSourceName()
					+ " is not an interface", null);
			throw new UnableToCompleteException();
		}

		final TreeLogger proxyLogger = logger.branch(TreeLogger.DEBUG,
				"Generating client proxy for remote service interface '"
						+ remoteService.getQualifiedSourceName() + QUOTE, null);

		final SessionProxyCreator proxyCreator = new SessionProxyCreator(
				remoteService);

		String result = proxyCreator.create(proxyLogger, ctx);

		if (remoteService.getAnnotation(RemoteServicePath.class) != null) {
			RemoteServiceProviderCreator providerCreator = new RemoteServiceProviderCreator(remoteService);
			providerCreator.create(proxyLogger, ctx);
			final RemoteServiceWrapperCreator wrapperCreator = new RemoteServiceWrapperCreator(remoteService);
			return wrapperCreator.create(proxyLogger, ctx);
		}
		
		return result;
	}
}