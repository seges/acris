package sk.seges.sesam.pap.configuration;

import sk.seges.sesam.pap.model.DomainObject;
import sk.seges.sesam.pap.model.MockEntityDto;
import sk.seges.sesam.pap.model.MockEntityDtoConverter;
import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = DomainObject.class, dtoClass = MockEntityDto.class, converter = MockEntityDtoConverter.class)
@Mapping(MappingType.EXPLICIT)
public interface MockEntityDtoConfiguration {
	
	void field1();

	void field2();

	@Field("reference.field1")
	String referenceField1();
}
