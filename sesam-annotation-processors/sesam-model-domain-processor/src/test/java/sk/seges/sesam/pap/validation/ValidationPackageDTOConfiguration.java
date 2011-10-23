package sk.seges.sesam.pap.validation;

import sk.seges.sesam.pap.model.EntityWithValidation;
import sk.seges.sesam.pap.model.annotation.Annotations;
import sk.seges.sesam.pap.model.annotation.Copy;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.PropertyAccessor;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.validation.annotation.Size;

@TransferObjectMapping(domainClass = EntityWithValidation.class)
@Mapping(MappingType.AUTOMATIC)
@Copy(annotations = 
	@Annotations(accessor = PropertyAccessor.PROPERTY, packageOf = Size.class))
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public interface ValidationPackageDTOConfiguration {}