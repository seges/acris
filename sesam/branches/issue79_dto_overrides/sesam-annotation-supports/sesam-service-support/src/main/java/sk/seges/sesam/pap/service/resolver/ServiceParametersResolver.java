package sk.seges.sesam.pap.service.resolver;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.DefaultParametersResolver;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

public class ServiceParametersResolver extends DefaultParametersResolver {

	public ServiceParametersResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
	}

	protected ParameterElement getConverterProviderParameter() {
		return new ParameterElement(processingEnv.getTypeUtils().toMutableType(ConverterProvider.class), CONVERTER_PROVIDER_NAME, false);
	}
}