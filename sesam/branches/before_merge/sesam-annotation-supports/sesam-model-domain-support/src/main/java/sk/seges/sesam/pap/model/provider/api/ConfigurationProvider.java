package sk.seges.sesam.pap.model.provider.api;

import java.util.List;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;

public interface ConfigurationProvider {

	List<ConfigurationTypeElement> getConfigurationsForDomain(MutableTypeMirror domainType);

	List<ConfigurationTypeElement> getConfigurationsForDto(MutableTypeMirror dtoType);
}