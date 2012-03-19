package sk.seges.sesam.pap.model.model.api.domain;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public interface DomainDeclaredType extends DomainType, MutableDeclaredType {

	TypeElement asConfigurationElement();

	DtoDeclaredType getDto();

	DomainDeclaredType getSuperClass();

	DomainType getId(EntityResolver entityResolver);
	DomainType getDomainReference(String fieldName);

	ExecutableElement getIdMethod(EntityResolver entityResolver);
	ExecutableElement getGetterMethod(String fieldName);
	ExecutableElement getSetterMethod(String fieldName);

	MutableDeclaredType asMutable();
}
