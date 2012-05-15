package sk.seges.corpis.pap.model.configuration;

import sk.seges.corpis.pap.model.converter.MockBlobConverter;
import sk.seges.corpis.pap.model.dto.MockEntityDTO;
import sk.seges.corpis.pap.model.entity.MockEntity;
import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = MockEntity.class, dtoClass = MockEntityDTO.class)
@Mapping(MappingType.AUTOMATIC)
public interface MockEntityDTOConfiguration {

	@TransferObjectMapping(converter = MockBlobConverter.class)
	@Field("blob")
	String contentDetached();

}