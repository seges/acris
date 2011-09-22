package sk.seges.sesam.pap.model.context.api;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;

public interface ProcessorContext {

	public static final String LOCAL_CONVERTER_NAME = "converter";

	String getLocalConverterName();

	TypeMirror getDomainMethodReturnType();

	String getDomainFieldPath();

	String getDomainFieldName();

	NamedType getFieldType();

	String getFieldName();

	ConfigurationTypeElement getConfigurationTypeElement();
	ConverterTypeElement getConverter();

	Modifier getModifier();

	ExecutableElement getMethod();

	ExecutableElement getDomainMethod();
	
}