package sk.seges.sesam.shared.model.converter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class CachedConverter<DTO, DOMAIN> implements DtoConverter<DTO, DOMAIN> {

	protected MapConvertedInstanceCache cache;
	
	public CachedConverter(MapConvertedInstanceCache cache) {
		this.cache = cache;
	}

	protected abstract Class<? extends DOMAIN> getDomainClass();
	protected abstract Class<? extends DTO> getDtoClass();

	protected abstract DOMAIN createDomainInstance(Serializable id);
	protected abstract DTO createDtoInstance(Serializable id);

	@SuppressWarnings("unchecked")
	public <T extends Collection<DTO>> T toDto(Collection<?> domains, Class<T> targetClass) {
		if (domains == null) {
			return null;
		}
		
		T result;
		try {
			result = targetClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create collection instance for class " + domains.getClass().getCanonicalName(), e);
		}
		
		Iterator<?> iterator = domains.iterator();
		
		while (iterator.hasNext()) {
			result.add(toDto((DOMAIN)iterator.next()));
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public Collection<DTO> toDto(Collection<?> domains) {
		if (domains == null) {
			return null;
		}

		return toDto(domains, domains.getClass());
	}
	
	@SuppressWarnings("unchecked")
	public Map<?, ?> toDto(Map<?, ?> domains) {
		if (domains == null) {
			return null;
		}

		Map<Object, Object> result;
		try {
			result = domains.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + domains.getClass().getCanonicalName(), e);
		}
		
		for(Entry<?, ?> entry: domains.entrySet()) {
			Object key = entry.getKey();
			if (key != null) {
				if (key.getClass().equals(getDomainClass())) {
					key = toDto((DOMAIN)key);
				}
			}
			Object value = entry.getValue();
			if (value != null) {
				if (value.getClass().equals(getDomainClass())) {
					value = toDto((DOMAIN)value);
				}
			}
			result.put(key, value);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Collection<DOMAIN>> T fromDto(Collection<?> dtos, Class<T> targetClass) {
		if (dtos == null) {
			return null;
		}
		
		T result;
		try {
			result = targetClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create collection instance for class " + dtos.getClass().getCanonicalName(), e);
		}
		
		Iterator<?> iterator = dtos.iterator();
		
		while (iterator.hasNext()) {
			result.add(fromDto((DTO)iterator.next()));
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public Collection<DOMAIN> fromDto(Collection<?> dtos) {
		if (dtos == null) {
			return null;
		}
		
		return fromDto(dtos, dtos.getClass());
	}
	
	@SuppressWarnings("unchecked")
	public Map<?, ?> fromDto(Map<?, ?> dtos) {
		if (dtos == null) {
			return null;
		}

		Map<Object, Object> result;
		try {
			result = dtos.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + dtos.getClass().getCanonicalName(), e);
		}
		
		for(Entry<?, ?> entry: dtos.entrySet()) {
			Object key = entry.getKey();
			if (key != null) {
				if (key.getClass().equals(getDtoClass())) {
					key = fromDto((DTO)key);
				}
			}
			Object value = entry.getValue();
			if (value != null) {
				if (value.getClass().equals(getDomainClass())) {
					value = fromDto((DTO)value);
				}
			}
			result.put(key, value);
		}
		
		return result;		
	}

	public DTO getDtoInstance(Serializable id) {
		DTO result = null;
		
		if (id != null) {
			result = cache.getInstance(getDtoClass(), id);
		}
		
		if (result != null) {
			return result;
		}
		
		result = createDtoInstance(id);

		if (result != null) {
			cache.putInstance(result, id);
		}
		
		return result;
	}
	
	public DOMAIN getDomainInstance(Serializable id) {
		DOMAIN result = null;
		
		if (id != null) {
			result = cache.getInstance(getDomainClass(), id);
		}
		
		if (result != null) {
			return result;
		}
		
		result = createDomainInstance(id);

		if (result != null) {
			cache.putInstance(result, id);
		}
		
		return result;
	}
}