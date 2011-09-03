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

	@Override
	public DTO createDtoInstance(Object domainSource, Serializable id) {
		DTO result = createDtoInstance(id);

		if (result != null) {
			if (id != null) {
				cache.putInstance(result, id);
			}
			cache.putInstance(domainSource, result);
		}
		
		return result;
	}

	@Override
	public DOMAIN createDomainInstance(Object dtoSource, Serializable id) {
		DOMAIN result = createDomainInstance(id);

		if (result != null) {
			if (id != null) {
				cache.putInstance(result, id);
			}
			cache.putInstance(dtoSource, result);
		}
		
		return result;
	}

	/**
	 * Loads DTO instance from the cache
	 */
	@Override
	@SuppressWarnings("unchecked")
	public DTO getDtoInstance(Object domainSource, Serializable id) {
		DTO result = (DTO)cache.getInstance(domainSource);

		if (result != null) {
			return result;
		}
		
		if (id != null && domainSource != null) {
			result = (DTO) cache.getInstance(domainSource.getClass(), id);
		}
		
		return result;
	}
	
	/**
	 * Loads DOMAIN instance from the cache
	 */
	@Override
	@SuppressWarnings("unchecked")
	public DOMAIN getDomainInstance(Object dtoSource, Serializable id) {
		DOMAIN result = (DOMAIN)cache.getInstance(dtoSource);

		if (result != null) {
			return result;
		}
		
		if (id != null && dtoSource != null) {
			result = (DOMAIN) cache.getInstance(dtoSource.getClass(), id);
		}
		
		return result;
	}
}