package sk.seges.sesam.pap.customer;

import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = TestCustomer.class)
@Mapping(MappingType.EXPLICIT)
public interface CustomerDtoConfiguration {	

	@Field void contact();
}