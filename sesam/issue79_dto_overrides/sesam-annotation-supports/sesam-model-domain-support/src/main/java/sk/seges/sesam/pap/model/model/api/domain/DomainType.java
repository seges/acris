package sk.seges.sesam.pap.model.model.api.domain;

import java.util.List;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.api.HasConverter;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;

public interface DomainType extends MutableTypeMirror, HasConverter {

	List<ConfigurationTypeElement> getConfigurations();
	ConfigurationTypeElement getDomainDefinitionConfiguration();

	DtoType getDto();
}