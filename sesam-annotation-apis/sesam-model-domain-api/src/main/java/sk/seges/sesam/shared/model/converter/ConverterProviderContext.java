package sk.seges.sesam.shared.model.converter;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.shared.model.converter.api.ConverterProvider;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public abstract class ConverterProviderContext {

	protected List<ConverterProvider> availableConverterProviders = new ArrayList<ConverterProvider>();

	public void registerConverterProvider(ConverterProvider converterProvider) {
		availableConverterProviders.add(converterProvider);
	}
	
	public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDomain(DOMAIN domain) {
		if (domain == null) {
			return null;
		}
		
		for (ConverterProvider availableConverterProvider: availableConverterProviders) {
			DtoConverter<DTO, DOMAIN> result = availableConverterProvider.getConverterForDomain(domain);
			
			if (result != null) {
				return result;
			}
		}

		return null;
	}

	public <DTO, DOMAIN> DtoConverter<DTO, DOMAIN> getConverterForDto(DTO dto) {
		if (dto == null) {
			return null;
		}
		
		for (ConverterProvider availableConverterProvider: availableConverterProviders) {
			DtoConverter<DTO, DOMAIN> result = availableConverterProvider.getConverterForDto(dto);
			
			if (result != null) {
				return result;
			}
		}

		return null;
	}
}