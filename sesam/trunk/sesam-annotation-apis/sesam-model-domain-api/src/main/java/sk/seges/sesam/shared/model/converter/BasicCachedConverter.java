package sk.seges.sesam.shared.model.converter;

import java.io.Serializable;

import sk.seges.sesam.shared.model.converter.api.CachedConverter;

public abstract class BasicCachedConverter<DTO, DOMAIN> implements CachedConverter<DTO, DOMAIN>{

	protected MapConvertedInstanceCache cache;
	
	public BasicCachedConverter(MapConvertedInstanceCache cache) {
		this.cache = cache;
	}

	protected abstract DOMAIN createDomainInstance(Serializable id);
	protected abstract DTO createDtoInstance(Serializable id);

	protected DTO putDtoIntoCache(Object domainSource, DTO result, Serializable id) {
		if (result != null) {
			if (id != null) {
				cache.putInstance(result, id);
			}
			cache.putInstance(domainSource, result);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	protected DTO getDtoFromCache(Object domainSource, Serializable id) {
		DTO result = (DTO)cache.getInstance(domainSource);

		if (result != null) {
			return result;
		}
		
		if (id != null && domainSource != null) {
			result = (DTO) cache.getInstance(domainSource.getClass(), id);
		}
		
		return result;
	}
	
	protected DOMAIN putDomainIntoCache(Object dtoSource, DOMAIN result, Serializable id) {
		if (result != null) {
			if (id != null) {
				cache.putInstance(result, id);
			}
			cache.putInstance(dtoSource, result);
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	protected DOMAIN getDomainFromCache(Object dtoSource, Serializable id) {
		DOMAIN result = (DOMAIN)cache.getInstance(dtoSource);

		if (result != null) {
			return result;
		}
		
		if (id != null && dtoSource != null) {
			result = (DOMAIN) cache.getInstance(dtoSource.getClass(), id);
		}
		
		return result;
	}
	
	@Override
	public DTO createDtoInstance(Object domainSource, Serializable id) {
		return putDtoIntoCache(domainSource, createDtoInstance(id), id);
	}

	@Override
	public DOMAIN createDomainInstance(Object dtoSource, Serializable id) {
		return putDomainIntoCache(dtoSource, createDomainInstance(id), id);
	}

	/**
	 * Loads DTO instance from the cache
	 */
	@Override
	public DTO getDtoInstance(Object domainSource, Serializable id) {
		return getDtoFromCache(domainSource, id);
	}
	
	/**
	 * Loads DOMAIN instance from the cache
	 */
	@Override
	public DOMAIN getDomainInstance(Object dtoSource, Serializable id) {
		return getDomainFromCache(dtoSource, id);
	}	
}