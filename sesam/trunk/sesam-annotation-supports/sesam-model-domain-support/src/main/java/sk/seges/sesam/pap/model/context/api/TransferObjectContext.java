package sk.seges.sesam.pap.model.context.api;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;

public interface TransferObjectContext {

	public static final String LOCAL_CONVERTER_NAME = "converter";

	TypeMirror getDomainMethodReturnType();

	String getDomainFieldPath();

	String getDomainFieldName();

	DtoType getFieldType();

	String getFieldName();

	ConfigurationTypeElement getConfigurationTypeElement();

	Modifier getModifier();

	ExecutableElement getDtoMethod();

	ExecutableElement getDomainMethod();

	/** Converter stuff */
	String getLocalConverterName();
	ConverterTypeElement getConverter();

}