package sk.seges.sesam.core.pap.configuration.api;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;

public interface ConfigurationElement {
	
	String name();
	
	boolean hasAnnotationOnField(VariableElement element);
	
	ElementKind getKind();	
	
	boolean isAditive();

	String getKey();
}