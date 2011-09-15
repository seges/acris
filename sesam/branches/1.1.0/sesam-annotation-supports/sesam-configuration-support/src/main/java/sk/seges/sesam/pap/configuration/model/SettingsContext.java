package sk.seges.sesam.pap.configuration.model;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.core.pap.model.api.NamedType;

public class SettingsContext {

	private TypeElement configurationElement;
	private ExecutableElement method;
	private String fieldName;

	private Parameter parameter;
//	private AnnotationMirror annotationMirror;
	
	private TypeElement nestedElement;
	private NamedType nestedOutputName;

	public void setConfigurationElement(TypeElement configurationElement) {
		this.configurationElement = configurationElement;
	}
	
	public TypeElement getConfigurationElement() {
		return configurationElement;
	}
	
	public void setMethod(ExecutableElement method) {
		this.method = method;
	}
	
	public ExecutableElement getMethod() {
		return method;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public Parameter getParameter() {
		return parameter;
	}
	
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public void setNestedElement(TypeElement nestedElement) {
		this.nestedElement = nestedElement;
	}
	
	public TypeElement getNestedElement() {
		return nestedElement;
	}
	
	public void setNestedOutputName(NamedType nestedOutputName) {
		this.nestedOutputName = nestedOutputName;
	}
	
	public NamedType getNestedOutputName() {
		return nestedOutputName;
	}

//	public AnnotationMirror getAnnotationMirror() {
//		return annotationMirror;
//	}
//
//	public void setAnnotationMirror(AnnotationMirror annotationMirror) {
//		this.annotationMirror = annotationMirror;
//	}
}