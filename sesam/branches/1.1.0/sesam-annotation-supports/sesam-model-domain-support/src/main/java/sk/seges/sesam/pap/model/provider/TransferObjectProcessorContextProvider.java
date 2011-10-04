package sk.seges.sesam.pap.model.provider;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import sk.seges.sesam.pap.model.context.TransferObjectProcessorContext;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class TransferObjectProcessorContextProvider {
	
	protected final TransferObjectProcessingEnvironment processingEnv;
	protected final RoundEnvironment roundEnv;
	protected final EntityResolver entityResolver;
	
	public TransferObjectProcessorContextProvider(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, EntityResolver entityResolver) {
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.entityResolver = entityResolver;
	}

	protected TransferObjectProcessorContext createContext(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod) {
		return new TransferObjectProcessorContext(configurationTypeElement, modifier, method, domainMethod);
	}
	
	public TransferObjectContext get(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod, String path, ConfigurationProvider[] configurationProviders) {

		TransferObjectProcessorContext processorContext = createContext(configurationTypeElement, modifier, method, domainMethod);
		if (!processorContext.initialize(processingEnv, roundEnv, entityResolver, path, configurationProviders)) {
			return null;
		}

		return processorContext;
	}
	
	public TransferObjectContext get(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod, ConfigurationProvider[] configurationProviders) {
		TransferObjectProcessorContext processorContext = createContext(configurationTypeElement, 
				modifier, method, domainMethod);
		if (!processorContext.initialize(processingEnv, roundEnv, entityResolver, configurationProviders)) {
			return null;
		}
		return processorContext;
	}
}