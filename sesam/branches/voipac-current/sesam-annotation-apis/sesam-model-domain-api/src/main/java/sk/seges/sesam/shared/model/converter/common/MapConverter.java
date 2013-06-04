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
package sk.seges.sesam.shared.model.converter.common;

import java.util.Map;
import java.util.Map.Entry;

import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class MapConverter<DTO_KEY, DTO_VALUE, DOMAIN_KEY, DOMAIN_VALUE> implements DtoConverter<Map<DTO_KEY, DTO_VALUE>, Map<DOMAIN_KEY, DOMAIN_VALUE>> {

	private final ConverterProvider converterProvider;
	private final ConvertedInstanceCache cache;
	

	public MapConverter(ConvertedInstanceCache cache, ConverterProvider converterProvider) {
		this.converterProvider = converterProvider;
		this.cache = cache;
	}

	@SuppressWarnings("unchecked")
	public <T extends Map<DTO_KEY, DTO_VALUE>> T toDto(Map<DOMAIN_KEY, DOMAIN_VALUE> domains, Class<T> targetClass) {
		if (domains == null) {
			return null;
		}
		
		T result;
		try {
			result = targetClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + domains.getClass().getCanonicalName(), e);
		}
		return (T) convertToDto(result, domains);
	}
	
	@SuppressWarnings("unchecked")
	public Map<DTO_KEY, DTO_VALUE> toDto(Map<DOMAIN_KEY, DOMAIN_VALUE> domains) {
		if (domains == null) {
			return null;
		}

		Map<DTO_KEY, DTO_VALUE> result;
		try {
			result = domains.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + domains.getClass().getCanonicalName(), e);
		}
		
		return convertToDto(result, domains);
	}

	@SuppressWarnings("unchecked")
	public Map<DOMAIN_KEY, DOMAIN_VALUE> fromDto(Map<DTO_KEY, DTO_VALUE> dtos) {
		if (dtos == null) {
			return null;
		}

		Map<DOMAIN_KEY, DOMAIN_VALUE> result;
		try {
			result = dtos.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + dtos.getClass().getCanonicalName(), e);
		}
		
		return convertFromDto(result, dtos);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<DTO_KEY, DTO_VALUE> convertToDto(Map<DTO_KEY, DTO_VALUE> result, Map<DOMAIN_KEY, DOMAIN_VALUE> domains) {
		for(Entry<DOMAIN_KEY, DOMAIN_VALUE> entry: domains.entrySet()) {
			DOMAIN_VALUE value = entry.getValue();
			DOMAIN_KEY key = entry.getKey();
			if (key != null) {
				DtoConverter<DTO_KEY, DOMAIN_KEY> keyConverter = converterProvider.getConverterForDomain(key, cache);
				DtoConverter<DTO_VALUE, DOMAIN_VALUE> valueConverter = converterProvider.getConverterForDomain(value, cache);
				
				result.put(keyConverter == null ? (DTO_KEY)key : keyConverter.toDto(key), 
						   valueConverter == null ? (DTO_VALUE)value : valueConverter.toDto(value));
			}
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<DOMAIN_KEY, DOMAIN_VALUE> convertFromDto(Map<DOMAIN_KEY, DOMAIN_VALUE> result, Map<DTO_KEY, DTO_VALUE> dtos) {
		for(Entry<DTO_KEY, DTO_VALUE> entry: dtos.entrySet()) {
			DTO_VALUE value = entry.getValue();
			DTO_KEY key = entry.getKey();
			if (key != null) {
				DtoConverter<DTO_KEY, DOMAIN_KEY> keyConverter = converterProvider.getConverterForDto(key, cache);
				DtoConverter<DTO_VALUE, DOMAIN_VALUE> valueConverter = converterProvider.getConverterForDto(value, cache);

				result.put(keyConverter == null ? (DOMAIN_KEY)key : keyConverter.fromDto(key), 
						   valueConverter == null ? (DOMAIN_VALUE)value : valueConverter.fromDto(value));
			}
		}
		
		return result;		
	}

	@Override
	public boolean equals(Map<DOMAIN_KEY, DOMAIN_VALUE> domain, Map<DTO_KEY, DTO_VALUE> dto) {
		return false;
	}
}