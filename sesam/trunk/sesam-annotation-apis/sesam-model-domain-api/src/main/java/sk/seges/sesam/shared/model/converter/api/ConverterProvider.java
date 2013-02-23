package sk.seges.sesam.shared.model.converter.api;

public interface ConverterProvider {
	
	<DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDomain(DOMAIN domain);
	<DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDomain(Class<DOMAIN> domainClass);

	<DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDto(DTO dto);
	<DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDto(Class<DTO> dtoClass);
}