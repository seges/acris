package sk.seges.sesam.pap.model.resolver.api;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;

public interface EntityResolver {

	TypeMirror getTargetEntityType(Element element);

	boolean shouldHaveIdMethod(ConfigurationTypeElement configurationElement, TypeMirror domainType);
	
	boolean isIdField(VariableElement field);
	boolean isIdMethod(ExecutableElement method);
}