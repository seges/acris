package sk.seges.sesam.pap.model.provider.api;

import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;

public interface ConfigurationProvider {

	ConfigurationTypeElement getConfigurationForDomain(TypeMirror domainType);

	ConfigurationTypeElement getConfigurationForDto(TypeMirror dtoType);
}