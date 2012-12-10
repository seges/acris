package sk.seges.sesam.pap.model.resolver;

import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

public interface ConverterConstructorParametersResolverProvider {

	public enum UsageType {
		DEFINITION,
		CONSTRUCTOR_CONVERTER_PROVIDER,
		USAGE_CONSTRUCTOR_CONVERTER_PROVIDER,
		USAGE_INSIDE_CONVERTER_PROVIDER, 
		USAGE_OUTSIDE_CONVERTER_PROVIDER;
	}
	
	ConverterConstructorParametersResolver getParameterResolver(UsageType usageType);
}
