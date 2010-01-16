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

public class SessionServiceInterfaceProxyGenerator extends Generator {

	private static final String QUOTE = "'";

	public SessionServiceInterfaceProxyGenerator() {
	}
	
	/**
	 * @param logger
	 * 			logger
	 * @param ctx
	 * 			context
	 * @param requestedClass
	 * 			class
	 * 
	 * @throws UnableToCompleteException
	 * 			exception
	 * 
	 * @return client-side proxy class
	 */
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

		final SessionProxyCreator proxyCreator = new SessionProxyCreator(
				remoteService);

		final RemoteServiceProviderCreator providerCreator = new RemoteServiceProviderCreator(remoteService);
		final RemoteServiceWrapperCreator wrapperCreator = new RemoteServiceWrapperCreator(remoteService);

		final TreeLogger proxyLogger = logger.branch(TreeLogger.DEBUG,
				"Generating client proxy for remote service interface '"
						+ remoteService.getQualifiedSourceName() + QUOTE, null);

		String result = proxyCreator.create(proxyLogger, ctx);

		if (remoteService.getAnnotation(RemoteServicePath.class) != null) {
			/*String resultProvider = */providerCreator.create(proxyLogger, ctx);
			return wrapperCreator.create(proxyLogger, ctx);
		}
		
		return result;
	}
}