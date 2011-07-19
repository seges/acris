package sk.seges.sesam.pap.model;

import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectConfiguration;

@TransferObjectConfiguration(DomainObject.class)
@Mapping(MappingType.EXPLICIT)
public interface MockEntityDtoConfiguration {
	
	void field1();

	void field2();

	@Field(DomainObjectMetaModel.REFERENCE.FIELD1)
	String referenceField1();
}
