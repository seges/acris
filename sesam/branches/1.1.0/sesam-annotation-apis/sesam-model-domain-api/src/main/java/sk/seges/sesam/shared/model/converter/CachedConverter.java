package sk.seges.sesam.shared.model.converter;

import java.io.Serializable;

public abstract class CachedConverter<DTO, DOMAIN> extends BasicConverter<DTO, DOMAIN> {

	protected MapConvertedInstanceCache cache;
	
	public CachedConverter(MapConvertedInstanceCache cache) {
		this.cache = cache;
	}

	@Override
	public DTO createDtoInstance(Object domainSource, Serializable id) {
		DTO result = createDtoInstance(id);

		if (result != null) {
			cache.putInstance(result, id);
			cache.putInstance(domainSource, result);
		}
		
		return result;
	}

	@Override
	public DOMAIN createDomainInstance(Object dtoSource, Serializable id) {
		DOMAIN result = createDomainInstance(id);

		if (result != null) {
			cache.putInstance(result, id);
			cache.putInstance(dtoSource, result);
		}
		
		return result;
	}

	/**
	 * Loads DTO instance from the cache
	 */
	@Override
	public DTO getDtoInstance(Object domainSource, Serializable id) {
		@SuppressWarnings("unchecked")
		DTO result = (DTO)cache.getInstance(domainSource);

		if (result != null) {
			return result;
		}
		
		if (id != null) {
			result = cache.getInstance(getDtoClass(), id);
		}
		
		return result;
	}
	
	/**
	 * Loads DOMAIN instance from the cache
	 */
	@Override
	public DOMAIN getDomainInstance(Object dtoSource, Serializable id) {
		@SuppressWarnings("unchecked")
		DOMAIN result = (DOMAIN)cache.getInstance(dtoSource);

		if (result != null) {
			return result;
		}
		
		if (id != null) {
			result = cache.getInstance(getDomainClass(), id);
		}
		
		return result;
	}
}