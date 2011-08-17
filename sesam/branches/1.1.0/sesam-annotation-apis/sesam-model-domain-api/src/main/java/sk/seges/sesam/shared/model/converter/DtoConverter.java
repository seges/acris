package sk.seges.sesam.shared.model.converter;

import java.util.Collection;
import java.util.Map;


public interface DtoConverter<DTO, DOMAIN> {

	DTO convertToDto(DTO result, DOMAIN domain);
	DTO toDto(DOMAIN domain);
	Collection<DTO> toDto(Collection<?> domains);
	Map<?, ?> toDto(Map<?, ?> domains);
	
	DOMAIN convertFromDto(DOMAIN result, DTO domain);
	DOMAIN fromDto(DTO dto);
	Collection<DOMAIN> fromDto(Collection<?> dtos);
	Map<?, ?> fromDto(Map<?, ?> dtos);
}