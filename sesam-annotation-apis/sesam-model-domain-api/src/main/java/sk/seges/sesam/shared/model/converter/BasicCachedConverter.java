/**
   Copyright 2011 Seges s.r.o.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package sk.seges.sesam.shared.model.converter;

import java.io.Serializable;

import sk.seges.sesam.shared.model.converter.api.CachedConverter;
import sk.seges.sesam.shared.model.converter.api.InstantiableDtoConverter;

public abstract class BasicCachedConverter<DTO, DOMAIN> implements CachedConverter<DTO, DOMAIN>, InstantiableDtoConverter<DTO, DOMAIN> {

	protected ConvertedInstanceCache cache;
	
	public BasicCachedConverter(ConvertedInstanceCache cache) {
		this.cache = cache;
	}

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