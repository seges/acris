package sk.seges.sesam.pap.model.resolver;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

public class DefaultConverterConstructorParametersResolver implements ConverterConstructorParametersResolver {

	protected MutableProcessingEnvironment processingEnv;

	public static final String CONVERTER_PROVIDER_NAME = "converterProvider";
	public static final String CONVERTER_CACHE_NAME = "cache";

	public DefaultConverterConstructorParametersResolver(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	protected ParameterElement getConverterProviderParameter() {
		return new ParameterElement(processingEnv.getTypeUtils().toMutableType(ConverterProvider.class), CONVERTER_PROVIDER_NAME, true);
	}

	protected ParameterElement getConverterCacheParameter() {
		return new ParameterElement(processingEnv.getTypeUtils().toMutableType(ConvertedInstanceCache.class), CONVERTER_CACHE_NAME,  true);
	}

	@Override
	public ParameterElement[] getConstructorAditionalParameters() {

		ParameterElement[] variableElements = new ParameterElement[2];
		variableElements[0] = getConverterProviderParameter();
		variableElements[1] = getConverterCacheParameter();

		return variableElements;
	}
}