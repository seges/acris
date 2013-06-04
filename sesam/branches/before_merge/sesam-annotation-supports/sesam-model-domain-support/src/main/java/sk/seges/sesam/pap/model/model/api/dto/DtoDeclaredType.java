package sk.seges.sesam.pap.model.model.api.dto;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public interface DtoDeclaredType extends DtoType, MutableDeclaredType {

	DtoDeclaredType getSuperClass();

	boolean isInterface();
	
	ExecutableElement getIdMethod(EntityResolver entityResolver);
}