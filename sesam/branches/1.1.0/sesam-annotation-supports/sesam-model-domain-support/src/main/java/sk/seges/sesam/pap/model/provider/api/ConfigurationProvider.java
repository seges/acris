package sk.seges.sesam.pap.model.provider.api;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;

public interface ConfigurationProvider {

	ConfigurationTypeElement getConfigurationForDomain(MutableTypeMirror domainType);

	ConfigurationTypeElement getConfigurationForDto(MutableTypeMirror dtoType);
}