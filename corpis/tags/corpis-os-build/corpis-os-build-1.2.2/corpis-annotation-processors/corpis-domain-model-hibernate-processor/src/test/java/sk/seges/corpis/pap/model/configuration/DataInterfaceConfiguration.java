package sk.seges.corpis.pap.model.configuration;

import sk.seges.corpis.pap.model.model.MockData;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = MockData.class)
@Mapping(MappingType.AUTOMATIC)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public interface DataInterfaceConfiguration {}