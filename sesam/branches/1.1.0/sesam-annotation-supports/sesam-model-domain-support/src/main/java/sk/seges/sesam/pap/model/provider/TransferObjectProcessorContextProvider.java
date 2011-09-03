package sk.seges.sesam.pap.model.provider;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import sk.seges.sesam.pap.model.context.TransferObjectProcessorContext;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class TransferObjectProcessorContextProvider {
	
	protected final ProcessingEnvironment processingEnv;
	protected final RoundEnvironment roundEnv;
	protected final EntityResolver entityResolver;
	
	public TransferObjectProcessorContextProvider(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, EntityResolver entityResolver) {
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.entityResolver = entityResolver;
	}

	protected TransferObjectProcessorContext createContext(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod) {
		return new TransferObjectProcessorContext(configurationTypeElement, modifier, method, domainMethod);
	}
	
	public ProcessorContext get(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod, String path) {

		TransferObjectProcessorContext processorContext = createContext(configurationTypeElement, modifier, method, domainMethod);
		if (!processorContext.initialize(processingEnv, roundEnv, entityResolver, path)) {
			return null;
		}

		return processorContext;
	}
	
	public ProcessorContext get(ConfigurationTypeElement configurationTypeElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod) {
		TransferObjectProcessorContext processorContext = createContext(configurationTypeElement, 
				modifier, method, domainMethod);
		if (!processorContext.initialize(processingEnv, roundEnv, entityResolver)) {
			return null;
		}
		return processorContext;
	}
}