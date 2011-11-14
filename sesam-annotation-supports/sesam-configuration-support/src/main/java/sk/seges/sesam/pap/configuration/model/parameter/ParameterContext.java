package sk.seges.sesam.pap.configuration.model.parameter;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

public abstract class ParameterContext {

	private TypeElement configurationElement;
	private ExecutableElement method;
	private String fieldName;
	
	private TypeElement nestedElement;
	private MutableDeclaredType nestedMutableType;
	private boolean nestedElementExists = false;
	
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
	
	public void setNestedElement(TypeElement nestedElement) {
		this.nestedElement = nestedElement;
	}
	
	public TypeElement getNestedElement() {
		return nestedElement;
	}
	
	public void setNestedMutableType(MutableDeclaredType nestedMutableType) {
		this.nestedMutableType = nestedMutableType;
	}
	
	public MutableDeclaredType getNestedMutableType() {
		return nestedMutableType;
	}
	
	public void setNestedElementExists(boolean nestedElementExists) {
		this.nestedElementExists = nestedElementExists;
	}
	
	public boolean isNestedElementExists() {
		return nestedElementExists;
	}
}