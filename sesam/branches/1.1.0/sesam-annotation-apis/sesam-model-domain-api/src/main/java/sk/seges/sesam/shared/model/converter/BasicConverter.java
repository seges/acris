package sk.seges.sesam.shared.model.converter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.sesam.dao.PagedResult;

public abstract class BasicConverter<DTO, DOMAIN> implements DtoConverter<DTO, DOMAIN> {

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

	/**
	 * Use this method to load DTO instance from any storage, like cache, etc.
	 * In a default implementation it always returns null
	 */
	public DTO getDtoInstance(Object domainSource, Serializable id) {
		return null;
	}

	/**
	 * Use this method to load DOMAIN instance from any storage, like cache, etc.
	 * In a default implementation it always returns null
	 */
	public DOMAIN getDomainInstance(Object dtoSource, Serializable id) {
		return null;
	}
	
	public DTO createDtoInstance(Object domainSource, Serializable id) {
		return createDtoInstance(id);
	}
	
	public DOMAIN createDomainInstance(Object dtoSource, Serializable id) {
		return createDomainInstance(id);
	}
}