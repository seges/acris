package sk.seges.sesam.core.model.converter.provider;

import java.util.Collection;
import java.util.Map;

import sk.seges.sesam.pap.model.annotation.ConverterProviderDefinition;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;
import sk.seges.sesam.shared.model.converter.common.CollectionConverter;
import sk.seges.sesam.shared.model.converter.common.MapConverter;
import sk.seges.sesam.shared.model.converter.provider.AbstractConverterProvider;

@ConverterProviderDefinition
public class SesamCommonConverterProvider extends AbstractConverterProvider {

	private final ConverterProviderContext converterProviderContext;
	
	public SesamCommonConverterProvider(ConverterProviderContext converterProviderContext) {
		this.converterProviderContext = converterProviderContext;
	}
	
	public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDomain(Class<DOMAIN> domainClass) {

		if (domainClass == null) {
			return null;
		}

		if (Collection.class.isAssignableFrom(domainClass)) {
			return getCollectionConverter();
		}

		if (Map.class.isAssignableFrom(domainClass)) {
			return getMapConverter();
		}

		return null;
	}

	public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDto(Class<DTO> dtoClass) {

		if (dtoClass == null) {
			return null;
		}

		if (Collection.class.isAssignableFrom(dtoClass)) {
			return getCollectionConverter();
		}

		if (Map.class.isAssignableFrom(dtoClass)) {
			return getMapConverter();
		}

		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getCollectionConverter() {
		return new CollectionConverter(converterProviderContext);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getMapConverter() {
		return new MapConverter(converterProviderContext);
	}
}