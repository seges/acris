package sk.seges.sesam.shared.model.converter;

import java.io.Serializable;

public abstract class CachedConverter<DTO, DOMAIN> implements DtoConverter<DTO, DOMAIN> {

	protected MapConvertedInstanceCache cache;
	
	public CachedConverter(MapConvertedInstanceCache cache) {
		this.cache = cache;
	}

	protected abstract Class<? extends DOMAIN> getDomainClass();
	protected abstract Class<? extends DTO> getDtoClass();

	protected abstract DOMAIN createDomainInstance(Serializable id);
	protected abstract DTO createDtoInstance(Serializable id);

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