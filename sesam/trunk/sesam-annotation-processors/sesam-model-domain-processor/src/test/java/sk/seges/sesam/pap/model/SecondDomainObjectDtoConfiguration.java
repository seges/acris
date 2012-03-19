package sk.seges.sesam.pap.model;

import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = SecondDomainObject.class)
@Mapping(MappingType.EXPLICIT)
public interface SecondDomainObjectDtoConfiguration {	
}