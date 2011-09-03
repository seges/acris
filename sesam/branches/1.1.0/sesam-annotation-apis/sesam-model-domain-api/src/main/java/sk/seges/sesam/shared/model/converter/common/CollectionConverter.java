package sk.seges.sesam.shared.model.converter.common;

import java.util.Collection;
import java.util.Iterator;

import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class CollectionConverter<DTO, DOMAIN> implements DtoConverter<Collection<DTO>, Collection<DOMAIN>> {

	private DtoConverter<DTO, DOMAIN> converter;

	public CollectionConverter(DtoConverter<DTO, DOMAIN> converter) {
		this.converter = converter;
	}

	@SuppressWarnings("unchecked")
	public <T extends Collection<DTO>> T toDto(Collection<DOMAIN> domains, Class<T> targetClass) {
		if (domains == null) {
			return null;
		}
		
		T result;
		try {
			result = targetClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create collection instance for class " + domains.getClass().getCanonicalName(), e);
		}
		
		return (T) convertToDto(result, domains);
	}

	@SuppressWarnings("unchecked")
	public Collection<DTO> toDto(Collection<DOMAIN> domains) {
		if (domains == null) {
			return null;
		}

		return toDto(domains, domains.getClass());
	}

	@SuppressWarnings("unchecked")
	public <T extends Collection<DOMAIN>> T fromDto(Collection<DTO> dtos, Class<T> targetClass) {
		if (dtos == null) {
			return null;
		}
		
		T result;
		try {
			result = targetClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create collection instance for class " + dtos.getClass().getCanonicalName(), e);
		}

		return (T) convertFromDto(result, dtos);
	}

	@SuppressWarnings("unchecked")
	public Collection<DOMAIN> fromDto(Collection<DTO> dtos) {
		if (dtos == null) {
			return null;
		}
		
		return fromDto(dtos, dtos.getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DTO> convertToDto(Collection<DTO> result, Collection<DOMAIN> domains) {
		Iterator<?> iterator = null;
		
		try {
			iterator = domains.iterator();
		} catch (Exception e) {
			return result;
		}
			
		while (iterator.hasNext()) {
			result.add(converter.toDto((DOMAIN)iterator.next()));
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DOMAIN> convertFromDto(Collection<DOMAIN> result, Collection<DTO> dtos) {
		Iterator<?> iterator = dtos.iterator();
		
		while (iterator.hasNext()) {
			DOMAIN domain = converter.fromDto((DTO)iterator.next());
			result.add(domain);
		}
		
		return result;
	}
}