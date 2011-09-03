package sk.seges.sesam.shared.model.converter.common;

import java.util.Map;
import java.util.Map.Entry;

import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class MapKeysConverter<DTO, DOMAIN> implements DtoConverter<Map<DTO, Object>, Map<DOMAIN, Object>> {

	private DtoConverter<DTO, DOMAIN> converter;

	public MapKeysConverter(DtoConverter<DTO, DOMAIN> converter) {
		this.converter = converter;
	}

	@SuppressWarnings("unchecked")
	public Map<DTO, Object> toDto(Map<DOMAIN, Object> domains) {
		if (domains == null) {
			return null;
		}

		Map<DTO, Object> result;
		try {
			result = domains.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + domains.getClass().getCanonicalName(), e);
		}
		
		return convertToDto(result, domains);
	}

	@SuppressWarnings("unchecked")
	public Map<DOMAIN, Object> fromDto(Map<DTO, Object> dtos) {
		if (dtos == null) {
			return null;
		}

		Map<DOMAIN, Object> result;
		try {
			result = dtos.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + dtos.getClass().getCanonicalName(), e);
		}
		
		return convertFromDto(result, dtos);
	}

	@Override
	public Map<DTO, Object> convertToDto(Map<DTO, Object> result, Map<DOMAIN, Object> domains) {
		for(Entry<DOMAIN, ?> entry: domains.entrySet()) {
			DOMAIN key = entry.getKey();
			if (key != null) {
				result.put(converter.toDto(key), entry.getValue());
			}
		}
		
		return result;
	}

	@Override
	public Map<DOMAIN, Object> convertFromDto(Map<DOMAIN, Object> result, Map<DTO, Object> dtos) {
		for(Entry<DTO, ?> entry: dtos.entrySet()) {
			DTO key = entry.getKey();
			if (key != null) {
				result.put(converter.fromDto((DTO)key), entry.getValue());
			}
		}
		
		return result;		
	}
}