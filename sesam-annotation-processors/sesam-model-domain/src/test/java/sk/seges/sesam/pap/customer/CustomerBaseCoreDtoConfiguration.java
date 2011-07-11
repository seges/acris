package sk.seges.sesam.pap.customer;

import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectConfiguration;


@TransferObjectConfiguration(TestCustomerBaseCore.class)
@Mapping(MappingType.AUTOMATIC)
public interface CustomerBaseCoreDtoConfiguration {

	@Ignore
	void address();

}