package sk.seges.sesam.shared.model.converter;

import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public abstract class BasicConverter<DTO, DOMAIN> implements DtoConverter<DTO, DOMAIN> {

	@Override
	public boolean equals(DOMAIN domain,DTO dto) {
		if (domain == null) {
			return (dto == null);
		}
		
		if (dto == null) {
			return false;
		}
		
		return domain.equals(dto);
	}
}