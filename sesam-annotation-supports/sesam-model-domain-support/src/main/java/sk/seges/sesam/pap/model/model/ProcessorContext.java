package sk.seges.sesam.pap.model.model;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.NamedType;

public class ProcessorContext {
	
	private TypeElement configurationElement;
	private Modifier modifier;
	private ExecutableElement method;
	private ExecutableElement domainMethod;

	private TypeMirror domainMethodReturnType;

	private NamedType fieldType;
	private String fieldName;

	private TypeElement domainTypeElement;
	private String domainFieldName;
	private String domainFieldPath;

	public ProcessorContext(TypeElement typeElement, Modifier modifier, ExecutableElement method) {
		this(typeElement, modifier, method, method);
	}

	public ProcessorContext(TypeElement configurationElement, Modifier modifier, ExecutableElement method,
			ExecutableElement domainMethod) {
		setConfigurationElement(configurationElement);
		setModifier(modifier);
		setMethod(method);
		setDomainMethod(domainMethod);
	}

	public void setDomainMethodReturnType(TypeMirror domainMethodReturnType) {
		this.domainMethodReturnType = domainMethodReturnType;
	}

	public TypeMirror getDomainMethodReturnType() {
		return domainMethodReturnType;
	}

	public TypeElement getDomainTypeElement() {
		return domainTypeElement;
	}

	public void setDomainTypeElement(TypeElement domainTypeElement) {
		this.domainTypeElement = domainTypeElement;
	}

	public String getDomainFieldPath() {
		return domainFieldPath;
	}

	public void setDomainFieldPath(String domainFieldPath) {
		this.domainFieldPath = domainFieldPath;
	}

	public void setDomainFieldName(String domainFieldName) {
		this.domainFieldName = domainFieldName;
	}

	public String getDomainFieldName() {
		return domainFieldName;
	}

	public void setFieldType(NamedType fieldType) {
		this.fieldType = fieldType;
	}

	public NamedType getFieldType() {
		return fieldType;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setConfigurationElement(TypeElement configurationElement) {
		this.configurationElement = configurationElement;
	}

	public TypeElement getConfigurationElement() {
		return configurationElement;
	}

	public void setModifier(Modifier modifier) {
		this.modifier = modifier;
	}

	public Modifier getModifier() {
		return modifier;
	}

	public void setMethod(ExecutableElement method) {
		this.method = method;
	}

	public ExecutableElement getMethod() {
		return method;
	}

	public ExecutableElement getDomainMethod() {
		return domainMethod;
	}

	public void setDomainMethod(ExecutableElement domainMethod) {
		this.domainMethod = domainMethod;
	}
}