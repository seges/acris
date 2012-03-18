package sk.seges.sesam.pap.service.resolver;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.DefaultParametersResolver;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

public class ServiceParametersResolver extends DefaultParametersResolver {

	public ServiceParametersResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
	}

	@Override
	protected ParameterElement getConverterProviderParameter() {
		return new ParameterElement(processingEnv.getTypeUtils().toMutableType(ConverterProvider.class), CONVERTER_PROVIDER_NAME, false);
	}

//	@Override
//	public ParameterElement[] getConstructorAditionalParameters() {
//
////		if (!domainType.getKind().equals(TypeKind.DECLARED)) {
////			return new ParameterElement[0];
////		}
//
//		ParameterElement[] variableElements = new ParameterElement[1];
//		variableElements[0] = getConverterProviderParameter();
//
//		return variableElements;
//	}
}