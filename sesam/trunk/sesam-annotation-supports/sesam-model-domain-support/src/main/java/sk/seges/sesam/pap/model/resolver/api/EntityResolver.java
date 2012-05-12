package sk.seges.sesam.pap.model.resolver.api;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;

public interface EntityResolver {

	TypeMirror getTargetEntityType(Element element);

	boolean shouldHaveIdMethod(DomainDeclaredType domainTypeElement);
	
	boolean isIdField(VariableElement field);
	boolean isIdMethod(ExecutableElement method);
	
	boolean isImmutable(Element element);
}