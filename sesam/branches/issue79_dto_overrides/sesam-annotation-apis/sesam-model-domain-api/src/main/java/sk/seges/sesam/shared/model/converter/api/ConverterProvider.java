package sk.seges.sesam.shared.model.converter.api;

public interface ConverterProvider {
	
	<DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDomain(DOMAIN domain);

	<DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDto(DTO dto);
}