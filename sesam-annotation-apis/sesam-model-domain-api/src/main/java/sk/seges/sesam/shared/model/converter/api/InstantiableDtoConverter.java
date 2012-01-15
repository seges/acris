package sk.seges.sesam.shared.model.converter.api;

import java.io.Serializable;

public interface InstantiableDtoConverter<DTO, DOMAIN> extends DtoConverter<DTO, DOMAIN> {
	
	DTO createDtoInstance(Serializable id);
}