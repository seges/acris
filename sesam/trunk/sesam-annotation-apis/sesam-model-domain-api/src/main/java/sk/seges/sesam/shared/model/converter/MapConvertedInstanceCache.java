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
import java.util.HashMap;
import java.util.Map;

public class MapConvertedInstanceCache implements ConvertedInstanceCache {

	private Map<Class<?>, Map<Serializable, Object>> instanceDtoIdCache = new HashMap<Class<?>, Map<Serializable, Object>>();
	//DomainClass, Map<DomainId, DTO>
	private Map<Class<?>, Map<Object, Object>> instancesDtoCache = new HashMap<Class<?>, Map<Object, Object>>();
	//DomainClass, Map<Domain, DTO>

	private Map<Class<?>, Map<Serializable, Object>> instanceDomainIdCache = new HashMap<Class<?>, Map<Serializable, Object>>();
	private Map<Class<?>, Map<Object, Object>> instancesDomainCache = new HashMap<Class<?>, Map<Object, Object>>();

	@Override
	@SuppressWarnings("unchecked")
	public <S> S getDtoInstance(Object domain) {
		Map<Object, Object> map = instancesDtoCache.get(domain.getClass());

		if (map == null) {
			return null;
		}
		return (S) map.get(domain);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S getDtoInstance(Class<S> domainClass, Serializable domainId) {
		Map<Serializable, Object> map = instanceDtoIdCache.get(domainClass);
		if (map == null) {
			return null;
		}
		return (S) map.get(domainId);
	}

	@Override
	public <S> S putDtoInstance(Object dto, S domain) {
		Map<Object, Object> instances = (Map<Object, Object>) instancesDtoCache.get(domain.getClass());

		if (instances == null) {
			instances = new HashMap<Object, Object>();
			instancesDtoCache.put(domain.getClass(), instances);
		}

		//result -> dto
		instances.put(domain, dto);
		
		return domain;
	}

	@Override
	public <S> S putDtoInstance(Object dto, S domain, Serializable dtoId) {
		Map<Serializable, Object> instances = (Map<Serializable, Object>) instanceDtoIdCache.get(domain.getClass());
		
		if (instances == null) {
			instances = new HashMap<Serializable, Object>();
			instanceDtoIdCache.put(domain.getClass(), instances);
		}
		
		instances.put(dtoId, dto);
		
		return domain;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S getDomainInstance(Object dto) {
		Map<Object, Object> map = instancesDomainCache.get(dto.getClass());

		if (map == null) {
			return null;
		}
		return (S) map.get(dto);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S getDomainInstance(Class<S> dtoClass, Serializable dtoId) {
		Map<Serializable, Object> map = instanceDtoIdCache.get(dtoClass);
		if (map == null) {
			return null;
		}
		return (S) map.get(dtoId);
	}

	@Override
	public <S> S putDomainInstance(Object domain, S dto) {
		Map<Object, Object> instances = (Map<Object, Object>) instancesDomainCache.get(dto.getClass());

		if (instances == null) {
			instances = new HashMap<Object, Object>();
			instancesDomainCache.put(dto.getClass(), instances);
		}

		
		//result -> dto
		instances.put(dto, domain);
		
		return dto;
	}

	@Override
	public <S> S putDomainInstance(Object domain, S dto, Serializable domainId) {
		Map<Serializable, Object> instances = (Map<Serializable, Object>) instanceDomainIdCache.get(dto.getClass());
		
		if (instances == null) {
			instances = new HashMap<Serializable, Object>();
			instanceDomainIdCache.put(dto.getClass(), instances);
		}
		
		instances.put(domainId, domain);
		
		return dto;
	}
}