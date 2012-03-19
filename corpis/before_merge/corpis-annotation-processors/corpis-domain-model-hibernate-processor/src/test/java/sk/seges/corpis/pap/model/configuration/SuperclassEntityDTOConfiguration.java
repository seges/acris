package sk.seges.corpis.pap.model.configuration;

import sk.seges.corpis.pap.model.converter.MockBlobConverter;
import sk.seges.corpis.pap.model.entity.MockEntity;
import sk.seges.corpis.pap.model.model.MockDto;
import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = MockEntity.class)
@Mapping(MappingType.AUTOMATIC)
public abstract class SuperclassEntityDTOConfiguration extends MockDto {
	
	@TransferObjectMapping(converter = MockBlobConverter.class)
	@Field("blob")
	abstract String contentDetached();

}