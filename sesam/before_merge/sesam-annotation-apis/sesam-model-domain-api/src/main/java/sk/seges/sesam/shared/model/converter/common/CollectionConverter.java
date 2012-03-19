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

import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class CollectionConverter<DTO, DOMAIN> implements DtoConverter<Collection<DTO>, Collection<DOMAIN>> {

	private DtoConverter<DTO, DOMAIN> converter;

	public CollectionConverter(DtoConverter<DTO, DOMAIN> converter) {
		this.converter = converter;
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
			if (converter == null) {
				result.add((DTO)iterator.next());
			} else {
				result.add(converter.toDto((DOMAIN)iterator.next()));
			}
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DOMAIN> convertFromDto(Collection<DOMAIN> result, Collection<DTO> dtos) {
		Iterator<?> iterator = dtos.iterator();
		
		List<DOMAIN> removed = new ArrayList<DOMAIN>();
		
		Iterator<?> domainIterator = result.iterator();

		if (converter != null) {
			while (domainIterator.hasNext()) {
				removed.add((DOMAIN)domainIterator.next());
			}
		} else {
			result.clear();
		}
		
		while (iterator.hasNext()) {
			if (converter == null) {
				result.add((DOMAIN)iterator.next());
			} else {
				DTO dto = (DTO)iterator.next();
				DOMAIN domain = getDomain(result, dto);
				if (domain != null) {
					converter.convertFromDto(domain, dto);
					removed.remove(domain);
				} else {
					result.add(converter.fromDto(dto));
				}
			}
		}

		for (DOMAIN domain: removed) {
			result.remove(domain);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private DOMAIN getDomain(Collection<DOMAIN> domains, DTO dto) {
		if (dto == null) {
			return null;
		}
		
		if (converter == null) {
			Iterator<?> iterator = domains.iterator();
			
			while (iterator.hasNext()) {
				DOMAIN domain = (DOMAIN)iterator.next();
				if (dto.equals(domain)) {
					return domain;
				}
			}
		} else {
			Iterator<?> iterator = domains.iterator();
	
			while (iterator.hasNext()) {
				DOMAIN domain = (DOMAIN)iterator.next();
				if (converter.equals(domain, dto)) {
					return domain;
				}
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