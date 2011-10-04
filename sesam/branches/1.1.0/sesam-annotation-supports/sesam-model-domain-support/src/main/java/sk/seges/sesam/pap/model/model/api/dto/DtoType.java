package sk.seges.sesam.pap.model.model.api.dto;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.api.GeneratedClass;

public interface DtoType extends MutableTypeMirror, GeneratedClass {

	ConfigurationTypeElement getConfiguration();	

	ConverterTypeElement getConverter();
	
	DomainTypeElement getDomain();
}
