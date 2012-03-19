package sk.seges.sesam.pap.equals;

import sk.seges.sesam.pap.model.Entity;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;

@TransferObjectMapping(domainClass = Entity.class)
@Mapping(MappingType.AUTOMATIC)
@GenerateHashcode(generate = false)
public interface EntityWithEqualsDTOConfiguration {}