package sk.seges.sesam.pap.model.model.api.dto;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public interface DtoDeclaredType extends DtoType, MutableDeclaredType {

	DomainDeclaredType getDomain();

	DtoDeclaredType getSuperClass();

	boolean isInterface();
	
	MutableExecutableElement getIdMethod(EntityResolver entityResolver);
}