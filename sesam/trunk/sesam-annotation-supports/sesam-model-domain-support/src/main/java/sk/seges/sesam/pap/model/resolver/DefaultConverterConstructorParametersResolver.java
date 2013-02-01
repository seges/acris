package sk.seges.sesam.pap.model.resolver;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;
import sk.seges.sesam.shared.model.converter.MapConvertedInstanceCache;

public class DefaultConverterConstructorParametersResolver implements ConverterConstructorParametersResolver {

	protected MutableProcessingEnvironment processingEnv;

	public static final String CONVERTER_PROVIDER_CONTEXT_NAME = "converterProviderContext";
	public static final String CONVERTER_CACHE_NAME = "cache";

	public DefaultConverterConstructorParametersResolver(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	protected ParameterElement getConverterProviderContextParameter() {
		return new ParameterElement(processingEnv.getTypeUtils().toMutableType(ConverterProviderContext.class), CONVERTER_PROVIDER_CONTEXT_NAME, 
				getConverterProviderContextReference(), isConverterProviderContextParameterPropagated(), processingEnv);
	}

	protected boolean isConverterProviderContextParameterPropagated() {
		return true;
	}
	
	protected ParameterElement getConverterCacheParameter() {
		return new ParameterElement(processingEnv.getTypeUtils().toMutableType(ConvertedInstanceCache.class), CONVERTER_CACHE_NAME, 
				getConverterCacheReference(), isConverterCacheParameterPropagated(), processingEnv);
	}
	
	protected boolean isConverterCacheParameterPropagated() {
		return true;
	}

	protected MutableReferenceType getConverterProviderContextReference() {
		return processingEnv.getTypeUtils().getReference(null, CONVERTER_PROVIDER_CONTEXT_NAME);
	}

	protected MutableReferenceType getConverterCacheReference() {
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		MutableDeclaredType cacheInstance = typeUtils.toMutableType(MapConvertedInstanceCache.class);
		
		return typeUtils.getReference(typeUtils.getTypeValue(cacheInstance, new MapConvertedInstanceCache()), CONVERTER_CACHE_NAME, true);
	}

	@Override
	public ParameterElement[] getConstructorAditionalParameters() {

		ParameterElement[] variableElements = new ParameterElement[2];
		variableElements[0] = getConverterCacheParameter();
		variableElements[1] = getConverterProviderContextParameter();

		return variableElements;
	}
}