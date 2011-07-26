package sk.seges.sesam.shared.model.converter;


public interface DtoConverter<DTO, DOMAIN> {

//	DTO getDtoInstance(Serializable id);
//	
//	DOMAIN getDomainInstance(Serializable id);

	DTO toDto(DOMAIN domain);
	
	DOMAIN fromDto(DTO dto);
}