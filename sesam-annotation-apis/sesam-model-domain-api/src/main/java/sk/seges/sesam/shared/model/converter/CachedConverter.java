package sk.seges.sesam.shared.model.converter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.sesam.dao.PagedResult;

public abstract class CachedConverter<DTO, DOMAIN> implements DtoConverter<DTO, DOMAIN> {

	protected MapConvertedInstanceCache cache;
	
	public CachedConverter(MapConvertedInstanceCache cache) {
		this.cache = cache;
	}

	protected abstract Class<? extends DOMAIN> getDomainClass();
	protected abstract Class<? extends DTO> getDtoClass();

	protected abstract DOMAIN createDomainInstance(Serializable id);
	protected abstract DTO createDtoInstance(Serializable id);

	@Override
	public PagedResult<? extends Collection<DTO>> toDto(PagedResult<? extends Collection<DOMAIN>> pagedDomain) {
		PagedResult<Collection<DTO>> result = new PagedResult<Collection<DTO>>();
		result.setPage(pagedDomain.getPage());
		result.setTotalResultCount(pagedDomain.getTotalResultCount());
		if (pagedDomain.getResult() != null) {
			result.setResult(toDto(pagedDomain.getResult()));
		}
		
		return result;
	}

	@Override
	public PagedResult<? extends Collection<DOMAIN>> fromDto(PagedResult<? extends Collection<DTO>> pagedDtos) {
		PagedResult<Collection<DOMAIN>> result = new PagedResult<Collection<DOMAIN>>();
		result.setPage(pagedDtos.getPage());
		result.setTotalResultCount(pagedDtos.getTotalResultCount());
		if (pagedDtos.getResult() != null) {
			result.setResult(fromDto(pagedDtos.getResult()));
		}
		return result;
	}
	
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
		
		Iterator<?> iterator = null;
		
		try {
			iterator = domains.iterator();
		} catch (Exception e) {
			return result;
		}
			
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

	public DTO getDtoInstance(Object domainSource, Serializable id) {
		DTO result = createDtoInstance(id);

		if (result != null) {
			cache.putInstance(result, id);
			cache.putInstance(domainSource, result);
		}
		
		return result;
	}
	
	public DTO getCachedDtoInstance(Object domainSource, Serializable id) {
		DTO result = null;

		result = cache.getInstance(domainSource);

		if (result != null) {
			return result;
		}
		
		if (id != null) {
			result = cache.getInstance(getDtoClass(), id);
		}
		
		return result;
	}

	public DOMAIN getDomainInstance(Object dtoSource, Serializable id) {
		DOMAIN result = createDomainInstance(id);

		if (result != null) {
			cache.putInstance(result, id);
			cache.putInstance(dtoSource, result);
		}
		
		return result;
	}
	
	protected DOMAIN getCachedDomainInstance(Object dtoSource, Serializable id) {
		DOMAIN result = null;
		
		result = cache.getInstance(dtoSource);

		if (id != null) {
			result = cache.getInstance(getDomainClass(), id);
		}
		
		return result;
	}
}