package sk.seges.sesam.pap.configuration.model;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.configuration.annotation.Parameter;

public class ProcessorContext {

	private TypeElement configurationElement;
	private ExecutableElement method;
	private String fieldName;
	private Parameter parameter;
	
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

}
