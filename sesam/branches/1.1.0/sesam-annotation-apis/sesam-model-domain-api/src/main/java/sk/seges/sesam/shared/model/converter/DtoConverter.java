package sk.seges.sesam.shared.model.converter;

import java.util.Collection;
import java.util.Map;


public interface DtoConverter<DTO, DOMAIN> {

	DTO toDto(DOMAIN domain);
	Collection<DTO> toDto(Collection<DOMAIN> domains);
	Map<?, ?> toDto(Map<?, ?> domains);
	
	DOMAIN fromDto(DTO dto);
	Collection<DOMAIN> fromDto(Collection<DTO> dtos);
	Map<?, ?> fromDto(Map<?, ?> dtos);
}