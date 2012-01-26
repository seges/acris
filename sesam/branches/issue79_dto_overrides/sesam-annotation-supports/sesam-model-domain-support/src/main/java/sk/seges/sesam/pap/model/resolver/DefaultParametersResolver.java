package sk.seges.sesam.pap.model.resolver;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

public class DefaultParametersResolver implements ParametersResolver {

	protected MutableProcessingEnvironment processingEnv;
	public static final String CONVERTER_PROVIDER_NAME = "converterProvider";

	public DefaultParametersResolver(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	protected ParameterElement getConverterProviderParameter() {
		return new ParameterElement(processingEnv.getTypeUtils().toMutableType(ConverterProvider.class), CONVERTER_PROVIDER_NAME, true);
	}
	
	@Override
	public ParameterElement[] getConstructorAditionalParameters() {
//		if (!domainType.getKind().equals(TypeKind.DECLARED)) {
//			return new ParameterElement[0];
//		}

		ParameterElement[] variableElements = new ParameterElement[1];
		variableElements[0] = getConverterProviderParameter();
		return variableElements;
	}
}