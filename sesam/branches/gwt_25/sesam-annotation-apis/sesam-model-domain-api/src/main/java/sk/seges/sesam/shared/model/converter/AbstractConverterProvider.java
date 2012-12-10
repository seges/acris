package sk.seges.sesam.shared.model.converter;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.shared.model.converter.api.ConverterProvider;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public abstract class AbstractConverterProvider implements ConverterProvider {

	protected List<AbstractConverterProvider> availableConverterProviders = new ArrayList<AbstractConverterProvider>();

	public void registerConverterProvider(AbstractConverterProvider converterProvider) {
		availableConverterProviders.add(converterProvider);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDomain(DOMAIN domain, ConvertedInstanceCache cache) {
		if (domain == null) {
			return null;
		}
		
		DtoConverter<DTO, DOMAIN> result = (DtoConverter<DTO, DOMAIN>) getConverterForDomain(domain.getClass(), cache);
		
		if (result != null) {
			return result;
		}
		
		for (AbstractConverterProvider availableConverterProvider: availableConverterProviders) {
			result = availableConverterProvider.getConverterForDomain(domain, cache);
			
			if (result != null) {
				return result;
			}
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDto(DTO dto, ConvertedInstanceCache cache) {
		if (dto == null) {
			return null;
		}
		
		DtoConverter<DTO, DOMAIN> result = (DtoConverter<DTO, DOMAIN>) getConverterForDto(dto.getClass(), cache);
		
		if (result != null) {
			return result;
		}
		
		for (AbstractConverterProvider availableConverterProvider: availableConverterProviders) {
			result = availableConverterProvider.getConverterForDto(dto, cache);
			
			if (result != null) {
				return result;
			}
		}
		
		return null;
	}
}