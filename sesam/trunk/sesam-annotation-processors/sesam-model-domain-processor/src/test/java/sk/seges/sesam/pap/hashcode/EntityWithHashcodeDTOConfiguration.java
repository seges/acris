package sk.seges.sesam.pap.hashcode;

import sk.seges.sesam.pap.model.Entity;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;

@TransferObjectMapping(domainClass = Entity.class)
@Mapping(MappingType.AUTOMATIC)
@GenerateEquals(generate = false)
public interface EntityWithHashcodeDTOConfiguration {}