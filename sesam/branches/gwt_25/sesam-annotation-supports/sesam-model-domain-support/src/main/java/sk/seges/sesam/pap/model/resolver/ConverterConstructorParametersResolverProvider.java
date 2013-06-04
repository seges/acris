package sk.seges.sesam.pap.model.resolver;

import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

public interface ConverterConstructorParametersResolverProvider {

	public enum UsageType {
		DEFINITION,	//TODO rename - CONVERTER_PROVIDER_DEFINITION? or SERVICE_DEFINITION?
		CONVERTER_PROVIDER_CONSTRUCTOR,
		CONVERTER_PROVIDER_CONSTRUCTOR_USAGE,
		CONVERTER_PROVIDER_INSIDE_USAGE, 
		CONVERTER_PROVIDER_OUTSIDE_USAGE,
		CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR;
	}
	
	ConverterConstructorParametersResolver getParameterResolver(UsageType usageType);
}
