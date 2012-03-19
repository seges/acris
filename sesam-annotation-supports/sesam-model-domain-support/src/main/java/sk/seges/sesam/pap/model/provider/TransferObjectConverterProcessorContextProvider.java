package sk.seges.sesam.pap.model.provider;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import sk.seges.sesam.pap.model.context.TransferObjectConverterProcessorContext;
import sk.seges.sesam.pap.model.context.TransferObjectProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class TransferObjectConverterProcessorContextProvider extends TransferObjectProcessorContextProvider {

	public TransferObjectConverterProcessorContextProvider(EnvironmentContext<TransferObjectProcessingEnvironment> envContext, EntityResolver entityResolver) {
		super(envContext, entityResolver);
	}

	@Override
	protected TransferObjectProcessorContext createContext(ConfigurationTypeElement configurationTypeElement, Modifier modifier,
			ExecutableElement method, ExecutableElement domainMethod) {
		return new TransferObjectConverterProcessorContext(configurationTypeElement, modifier, method, domainMethod);
	}
}
