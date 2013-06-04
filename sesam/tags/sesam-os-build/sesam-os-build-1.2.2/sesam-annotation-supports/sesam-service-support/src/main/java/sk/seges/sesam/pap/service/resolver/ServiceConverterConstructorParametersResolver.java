package sk.seges.sesam.pap.service.resolver;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.DefaultConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

public class ServiceConverterConstructorParametersResolver extends DefaultConverterConstructorParametersResolver {

	public ServiceConverterConstructorParametersResolver(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
	}

	@Override
	protected ParameterElement getConverterProviderParameter() {
		return new ParameterElement(processingEnv.getTypeUtils().toMutableType(ConverterProvider.class), CONVERTER_PROVIDER_NAME, false);
	}

	@Override
	protected ParameterElement getConverterCacheParameter() {
		return new ParameterElement(processingEnv.getTypeUtils().toMutableType(ConvertedInstanceCache.class), ConverterProviderPrinter.CONVERTER_CACHE_NAME, false);
	}

//	@Override
//	public ParameterElement[] getConstructorAditionalParameters() {
//		ParameterElement[] constructorAditionalParameters = super.getConstructorAditionalParameters();
//		
//		ParameterElement[] result = new ParameterElement[constructorAditionalParameters.length + 1];
//		
//		for (int i = 0; i < constructorAditionalParameters.length; i++) {
//			result[i] = constructorAditionalParameters[i];
//		}
//		
//		result[constructorAditionalParameters.length] = getConverterCacheParameter();
//		
//		return result;
//	}
}