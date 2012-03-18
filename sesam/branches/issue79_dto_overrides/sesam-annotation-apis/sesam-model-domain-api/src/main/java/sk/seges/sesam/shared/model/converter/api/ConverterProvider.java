package sk.seges.sesam.shared.model.converter.api;

import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;

public interface ConverterProvider {
	
	<DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDomain(DOMAIN domain, ConvertedInstanceCache cache);
	<DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDomain(Class<DOMAIN> domainClass, ConvertedInstanceCache cache);

	<DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDto(DTO dto, ConvertedInstanceCache cache);
	<DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDto(Class<DTO> dtoClass, ConvertedInstanceCache cache);
}