package sk.seges.sesam.pap.configuration.model;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;

public class SettingsContext {
	
	private TypeElement configurationElement;
	private ExecutableElement method;
	private String fieldName;

	private Parameter parameter;
	private String prefix = "";
	
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
	
	public String getParameterName() {
		if (parameter == null) {
			return null;
		}
		return prefix + parameter.name();
	}

	public String getParameterDescription() {
		if (parameter == null) {
			return null;
		}
		return parameter.description();
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
	
	public void setNestedMutableType(MutableDeclaredType nestedMutableType) {
		this.nestedMutableType = nestedMutableType;
	}
	
	public MutableDeclaredType getNestedMutableType() {
		return nestedMutableType;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setNestedElementExists(boolean nestedElementExists) {
		this.nestedElementExists = nestedElementExists;
	}
	
	public boolean isNestedElementExists() {
		return nestedElementExists;
	}
}