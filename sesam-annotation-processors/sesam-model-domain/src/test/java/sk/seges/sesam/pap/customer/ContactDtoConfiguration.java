package sk.seges.sesam.pap.customer;

import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectConfiguration;


@TransferObjectConfiguration(TestContactData.class)
@Mapping(MappingType.EXPLICIT)
public interface ContactDtoConfiguration {

	@Field
	void web();
}