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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class CollectionConverter<DTO, DOMAIN> implements DtoConverter<Collection<DTO>, Collection<DOMAIN>> {

	private final ConverterProvider converterProvider;
	private final ConvertedInstanceCache cache;
	
	public CollectionConverter(ConvertedInstanceCache cache, ConverterProvider converterProvider) {
		this.cache = cache;
		this.converterProvider = converterProvider;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Collection<DTO>> T toDto(Collection<DOMAIN> domains, Class<T> targetClass) {
		if (domains == null) {
			return null;
		}
		
		T result;
		try {
			result = targetClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create collection instance for class " + domains.getClass().getCanonicalName(), e);
		}
		
		return (T) convertToDto(result, domains);
	}

	@SuppressWarnings("unchecked")
	public Collection<DTO> toDto(Collection<DOMAIN> domains) {
		if (domains == null) {
			return null;
		}

		return toDto(domains, domains.getClass());
	}

	@SuppressWarnings("unchecked")
	public <T extends Collection<DOMAIN>> T fromDto(Collection<DTO> dtos, Class<T> targetClass) {
		if (dtos == null) {
			return null;
		}
		
		T result;
		try {
			result = targetClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create collection instance for class " + dtos.getClass().getCanonicalName(), e);
		}

		return (T) convertFromDto(result, dtos);
	}

	@SuppressWarnings("unchecked")
	public Collection<DOMAIN> fromDto(Collection<DTO> dtos) {
		if (dtos == null) {
			return null;
		}
		
		return fromDto(dtos, dtos.getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DTO> convertToDto(Collection<DTO> result, Collection<DOMAIN> domains) {
		Iterator<?> iterator = null;
		
		try {
			iterator = domains.iterator();
		} catch (Exception e) {
			return result;
		}
		
		while (iterator.hasNext()) {
			DOMAIN domain = (DOMAIN)iterator.next();
			
			DtoConverter<DTO, DOMAIN> converter = converterProvider.getConverterForDomain(domain, cache);
			
			if (converter == null) {
				result.add((DTO)domain);
			} else {
				result.add(converter.toDto(domain));
			}
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DOMAIN> convertFromDto(Collection<DOMAIN> result, Collection<DTO> dtos) {
		Iterator<?> dtoIterator = dtos.iterator();
		
		List<DOMAIN> originals = new ArrayList<DOMAIN>();
		
		Iterator<?> domainIterator = result.iterator();

		while (domainIterator.hasNext()) {
			originals.add((DOMAIN)domainIterator.next());
		}

		result.clear();
		
		while (dtoIterator.hasNext()) {
			DTO dto = (DTO)dtoIterator.next();

			DtoConverter<DTO, DOMAIN> converter = converterProvider.getConverterForDto(dto, cache);

			if (converter == null) {
				result.add((DOMAIN)dto);
			} else {
				DOMAIN domain = getDomain(originals, dto);
				if (domain != null) {
					result.add(converter.convertFromDto(domain, dto));
				} else {
					result.add(converter.fromDto(dto));
				}
			}
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	private DOMAIN getDomain(Collection<DOMAIN> domains, DTO dto) {
		if (dto == null) {
			return null;
		}

		Iterator<?> iterator = domains.iterator();

		while (iterator.hasNext()) {

			DOMAIN domain = (DOMAIN)iterator.next();
			DtoConverter<DTO, DOMAIN> converter = converterProvider.getConverterForDto(dto, cache);

			if (converter == null && dto.equals(domain)) {
				return domain;
			} else if (converter != null && converter.equals(domain, dto)) {
				return domain;
			}
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Collection<DOMAIN> domains, Collection<DTO> dtos) {
		if (domains == null) {
			if (dtos != null) {
				return false;
			}
			return true;
		}
		
		if (dtos == null) {
			return false;
		}
		
		if (domains.size() != dtos.size()) {
			return false;
		}

		Iterator<?> dtoIterator = dtos.iterator();
		Iterator<?> domainIterator = domains.iterator();

		List<DOMAIN> notProceeded = new ArrayList<DOMAIN>();
		
		while (domainIterator.hasNext()) {
			notProceeded.add((DOMAIN)domainIterator.next());
		}

		while (dtoIterator.hasNext()) {
			DOMAIN domain = getDomain(notProceeded, (DTO)dtoIterator.next());
			if (domain == null) {
				return false;
			}
			notProceeded.remove(domain);
		}
		
		if (notProceeded.size() > 0) {
			return false;
		}
		
		return true;
	}
}