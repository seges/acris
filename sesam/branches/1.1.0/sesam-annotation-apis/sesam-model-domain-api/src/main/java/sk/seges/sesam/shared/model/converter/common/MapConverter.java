package sk.seges.sesam.shared.model.converter.common;

import java.util.Map;
import java.util.Map.Entry;

import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class MapConverter<DTO_KEY, DTO_VALUE, DOMAIN_KEY, DOMAIN_VALUE> implements DtoConverter<Map<DTO_KEY, DTO_VALUE>, Map<DOMAIN_KEY, DOMAIN_VALUE>> {

	private DtoConverter<DTO_KEY, DOMAIN_KEY> keyConverter;
	private DtoConverter<DTO_VALUE, DOMAIN_VALUE> valueConverter;

	public MapConverter(DtoConverter<DTO_KEY, DOMAIN_KEY> keyConverter, DtoConverter<DTO_VALUE, DOMAIN_VALUE> valueConverter) {
		this.keyConverter = keyConverter;
		this.valueConverter = valueConverter;
	}

	@SuppressWarnings("unchecked")
	public Map<DTO_KEY, DTO_VALUE> toDto(Map<DOMAIN_KEY, DOMAIN_VALUE> domains) {
		if (domains == null) {
			return null;
		}

		Map<DTO_KEY, DTO_VALUE> result;
		try {
			result = domains.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + domains.getClass().getCanonicalName(), e);
		}
		
		return convertToDto(result, domains);
	}

	@SuppressWarnings("unchecked")
	public Map<DOMAIN_KEY, DOMAIN_VALUE> fromDto(Map<DTO_KEY, DTO_VALUE> dtos) {
		if (dtos == null) {
			return null;
		}

		Map<DOMAIN_KEY, DOMAIN_VALUE> result;
		try {
			result = dtos.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + dtos.getClass().getCanonicalName(), e);
		}
		
		return convertFromDto(result, dtos);
	}

	@Override
	public Map<DTO_KEY, DTO_VALUE> convertToDto(Map<DTO_KEY, DTO_VALUE> result, Map<DOMAIN_KEY, DOMAIN_VALUE> domains) {
		for(Entry<DOMAIN_KEY, DOMAIN_VALUE> entry: domains.entrySet()) {
			DOMAIN_VALUE value = entry.getValue();
			DOMAIN_KEY key = entry.getKey();
			if (key != null) {
				result.put(keyConverter.toDto(key), valueConverter.toDto(value));
			}
		}
		
		return result;
	}

	@Override
	public Map<DOMAIN_KEY, DOMAIN_VALUE> convertFromDto(Map<DOMAIN_KEY, DOMAIN_VALUE> result, Map<DTO_KEY, DTO_VALUE> dtos) {
		for(Entry<DTO_KEY, DTO_VALUE> entry: dtos.entrySet()) {
			DTO_VALUE value = entry.getValue();
			DTO_KEY key = entry.getKey();
			if (key != null) {
				result.put(keyConverter.fromDto(key), valueConverter.fromDto(value));
			}
		}
		
		return result;		
	}
}