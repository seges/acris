package sk.seges.sesam.shared.model.converter.api;



public interface DtoConverter<DTO, DOMAIN> {

	DTO convertToDto(DTO result, DOMAIN domain);
	DTO toDto(DOMAIN domain);
	
	DOMAIN convertFromDto(DOMAIN result, DTO dto);
	DOMAIN fromDto(DTO dto);

	boolean equals(DOMAIN domain, DTO dto);
}