package sk.seges.sesam.pap.model.provider;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import sk.seges.sesam.pap.model.context.TransferObjectProcessorContext;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class TransferObjectProcessorContextProvider {
	
	protected final EntityResolver entityResolver;
	protected final EnvironmentContext<TransferObjectProcessingEnvironment> envContext;
	
	public TransferObjectProcessorContextProvider(EnvironmentContext<TransferObjectProcessingEnvironment> envContext, EntityResolver entityResolver) {
		this.envContext = envContext;
		this.entityResolver = entityResolver;
	}

	protected TransferObjectProcessorContext createContext(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod) {
		return new TransferObjectProcessorContext(configurationTypeElement, modifier, method, domainMethod);
	}
	
	public TransferObjectContext get(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod, String path, ConfigurationProvider[] configurationProviders) {

		TransferObjectProcessorContext processorContext = createContext(configurationTypeElement, modifier, method, domainMethod);
		if (!processorContext.initialize(envContext, entityResolver, path)) {
			return null;
		}

		return processorContext;
	}
	
	public TransferObjectContext get(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod, ConfigurationProvider[] configurationProviders) {
		TransferObjectProcessorContext processorContext = createContext(configurationTypeElement, 
				modifier, method, domainMethod);
		if (!processorContext.initialize(envContext, entityResolver)) {
			return null;
		}
		return processorContext;
	}
}