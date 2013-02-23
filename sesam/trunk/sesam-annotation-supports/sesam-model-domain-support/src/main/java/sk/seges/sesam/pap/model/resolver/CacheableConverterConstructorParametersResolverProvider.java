package sk.seges.sesam.pap.model.resolver;

import java.util.HashMap;
import java.util.Map;

import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

public abstract class CacheableConverterConstructorParametersResolverProvider implements ConverterConstructorParametersResolverProvider {

	private Map<UsageType, ConverterConstructorParametersResolver> cache = new HashMap<ConverterConstructorParametersResolverProvider.UsageType, ConverterConstructorParametersResolver>();
	
	@Override
	public final ConverterConstructorParametersResolver getParameterResolver(UsageType usageType) {
		if (cache.containsKey(usageType)) {
			return cache.get(usageType);
		}
		
		ConverterConstructorParametersResolver constructParameterResolver = constructParameterResolver(usageType);
		cache.put(usageType, constructParameterResolver);
		
		return constructParameterResolver;
	}

	public abstract ConverterConstructorParametersResolver constructParameterResolver(UsageType usageType);
	
}