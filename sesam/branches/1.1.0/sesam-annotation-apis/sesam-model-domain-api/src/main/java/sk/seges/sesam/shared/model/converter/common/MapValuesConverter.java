package sk.seges.sesam.shared.model.converter.common;

import java.util.Map;
import java.util.Map.Entry;

import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class MapValuesConverter<DTO, DOMAIN> implements DtoConverter<Map<Object, DTO>, Map<Object, DOMAIN>> {

	private DtoConverter<DTO, DOMAIN> converter;

	public MapValuesConverter(DtoConverter<DTO, DOMAIN> converter) {
		this.converter = converter;
	}

	@SuppressWarnings("unchecked")
	public Map<Object, DTO> toDto(Map<Object, DOMAIN> domains) {
		if (domains == null) {
			return null;
		}

		Map<Object, DTO> result;
		try {
			result = domains.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + domains.getClass().getCanonicalName(), e);
		}
		
		return convertToDto(result, domains);
	}

	@SuppressWarnings("unchecked")
	public Map<Object, DOMAIN> fromDto(Map<Object, DTO> dtos) {
		if (dtos == null) {
			return null;
		}

		Map<Object, DOMAIN> result;
		try {
			result = dtos.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + dtos.getClass().getCanonicalName(), e);
		}
		
		return convertFromDto(result, dtos);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Object, DTO> convertToDto(Map<Object, DTO> result, Map<Object, DOMAIN> domains) {
		for(Entry<Object, DOMAIN> entry: domains.entrySet()) {
			DOMAIN value = entry.getValue();
			if (value != null) {
				result.put(entry.getKey(), converter == null ? (DTO)value : converter.toDto(value));
			}
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Object, DOMAIN> convertFromDto(Map<Object, DOMAIN> result, Map<Object, DTO> dtos) {
		for(Entry<Object, DTO> entry: dtos.entrySet()) {
			DTO value = entry.getValue();
			if (value != null) {
				result.put(entry.getKey(), converter == null ? (DOMAIN)value : converter.fromDto((DTO)value));
			}
		}
		
		return result;		
	}
}