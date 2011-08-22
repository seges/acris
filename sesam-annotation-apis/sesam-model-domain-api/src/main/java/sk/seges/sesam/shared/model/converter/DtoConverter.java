package sk.seges.sesam.shared.model.converter;

import java.util.Collection;
import java.util.Map;

import sk.seges.sesam.dao.PagedResult;


public interface DtoConverter<DTO, DOMAIN> {

	PagedResult<? extends Collection<DTO>> toDto(PagedResult<? extends Collection<DOMAIN>> pagedDomains);
	DTO convertToDto(DTO result, DOMAIN domain);
	DTO toDto(DOMAIN domain);
	Collection<DTO> toDto(Collection<?> domains);
	<T extends Collection<DTO>> T toDto(Collection<?> domains, Class<T> targetClass);
	Map<?, ?> toDto(Map<?, ?> domains);
	
	PagedResult<? extends Collection<DOMAIN>> fromDto(PagedResult<? extends Collection<DTO>> pagedDtos);
	DOMAIN convertFromDto(DOMAIN result, DTO domain);
	DOMAIN fromDto(DTO dto);
	Collection<DOMAIN> fromDto(Collection<?> dtos);
	<T extends Collection<DOMAIN>> T fromDto(Collection<?> dtos, Class<T> targetClass);
	Map<?, ?> fromDto(Map<?, ?> dtos);
}