package sk.seges.sesam.pap.model.model.api.domain;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;

public interface DomainType extends MutableTypeMirror {

	ConfigurationTypeElement getConfiguration();
	ConverterTypeElement getConverter();
	DtoType getDto();
}
