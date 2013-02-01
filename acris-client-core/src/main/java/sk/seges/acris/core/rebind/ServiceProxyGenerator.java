package sk.seges.acris.core.rebind;

import sk.seges.acris.core.client.annotation.RemoteServicePath;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.RebindResult;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator;

/**
 * Generator used to create:
 * <ul>
 * <li>Session aware remote proxy service</li>
 * <li>Session proxy automatically initialized using {@link RemoteServicePath} annotation</li>
 * </ul>
 * 
 * @author Peter Simun (simun@seges.sk)
 * 
 */
public class ServiceProxyGenerator extends ServiceInterfaceProxyGenerator {

	public ServiceProxyGenerator() {
	}

	@Override
	public RebindResult generateIncrementally(TreeLogger logger, GeneratorContext ctx, String requestedClass) throws UnableToCompleteException {
		
		final TypeOracle typeOracle = ctx.getTypeOracle();
	    assert (typeOracle != null);

		final JClassType remoteService = typeOracle.findType(requestedClass);

	    if (remoteService == null) {
	      logger.log(TreeLogger.ERROR, "Unable to find metadata for type '" + requestedClass + "'", null);
	      throw new UnableToCompleteException();
	    }

	    if (remoteService.isInterface() == null) {
	    	logger.log(TreeLogger.ERROR, remoteService.getQualifiedSourceName() + " is not an interface", null);
		    throw new UnableToCompleteException();
		}

		RebindResult result = super.generateIncrementally(logger, ctx, requestedClass);

		final TreeLogger proxyLogger = logger.branch(TreeLogger.DEBUG,
				"Generating client proxy for remote service interface '" + remoteService.getQualifiedSourceName() + "'", null);

		if (remoteService.getAnnotation(RemoteServicePath.class) != null) {
			RemoteServiceProviderCreator providerCreator = new RemoteServiceProviderCreator(remoteService);
			providerCreator.create(proxyLogger, ctx);
			RemoteServiceWrapperCreator wrapperCreator = new RemoteServiceWrapperCreator(remoteService);
			return wrapperCreator.create(proxyLogger, ctx);
		}

		return result;
	}
}