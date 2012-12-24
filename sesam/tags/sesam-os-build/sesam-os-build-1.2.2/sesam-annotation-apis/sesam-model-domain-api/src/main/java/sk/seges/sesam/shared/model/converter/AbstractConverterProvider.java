package sk.seges.sesam.shared.model.converter;

import sk.seges.sesam.shared.model.converter.api.ConverterProvider;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public abstract class AbstractConverterProvider implements ConverterProvider {

	@SuppressWarnings("unchecked")
	@Override
	public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDomain(DOMAIN domain, ConvertedInstanceCache cache) {
		if (domain == null) {
			return null;
		}
		
		return (DtoConverter<DTO, DOMAIN>) getConverterForDomain(domain.getClass(), cache);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDto(DTO dto, ConvertedInstanceCache cache) {
		if (dto == null) {
			return null;
		}
		
		return (DtoConverter<DTO, DOMAIN>) getConverterForDto(dto.getClass(), cache);
	}
}