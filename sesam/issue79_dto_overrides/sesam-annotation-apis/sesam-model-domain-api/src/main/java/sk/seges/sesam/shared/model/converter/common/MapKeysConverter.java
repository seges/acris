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

import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class MapKeysConverter<DTO, DOMAIN> implements DtoConverter<Map<DTO, Object>, Map<DOMAIN, Object>> {

	private DtoConverter<DTO, DOMAIN> converter;

	public MapKeysConverter(DtoConverter<DTO, DOMAIN> converter) {
		this.converter = converter;
	}

	@SuppressWarnings("unchecked")
	public Map<DTO, Object> toDto(Map<DOMAIN, Object> domains) {
		if (domains == null) {
			return null;
		}

		Map<DTO, Object> result;
		try {
			result = domains.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + domains.getClass().getCanonicalName(), e);
		}
		
		return convertToDto(result, domains);
	}

	@SuppressWarnings("unchecked")
	public Map<DOMAIN, Object> fromDto(Map<DTO, Object> dtos) {
		if (dtos == null) {
			return null;
		}

		Map<DOMAIN, Object> result;
		try {
			result = dtos.getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create map instance for class " + dtos.getClass().getCanonicalName(), e);
		}
		
		return convertFromDto(result, dtos);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<DTO, Object> convertToDto(Map<DTO, Object> result, Map<DOMAIN, Object> domains) {
		for(Entry<DOMAIN, ?> entry: domains.entrySet()) {
			DOMAIN key = entry.getKey();
			if (key != null) {
				result.put(converter == null ? (DTO)key : converter.toDto(key), entry.getValue());
			}
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<DOMAIN, Object> convertFromDto(Map<DOMAIN, Object> result, Map<DTO, Object> dtos) {
		for(Entry<DTO, ?> entry: dtos.entrySet()) {
			DTO key = entry.getKey();
			if (key != null) {
				result.put(converter == null ? (DOMAIN)key : converter.fromDto((DTO)key), entry.getValue());
			}
		}
		
		return result;		
	}

	@Override
	public boolean equals(Map<DOMAIN, Object> domains, Map<DTO, Object> dtos) {
		//TODO
		return false;
	}
}