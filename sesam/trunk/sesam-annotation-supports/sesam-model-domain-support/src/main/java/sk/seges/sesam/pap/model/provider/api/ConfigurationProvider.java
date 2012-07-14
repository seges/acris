package sk.seges.sesam.pap.model.provider.api;

import java.util.List;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;

public interface ConfigurationProvider {

	List<ConfigurationTypeElement> getConfigurationsForDomain(MutableTypeMirror domainType);

	List<ConfigurationTypeElement> getConfigurationsForDto(MutableTypeMirror dtoType);

	ConfigurationTypeElement getConfiguration(ExecutableElement configurationElementMethod, DomainDeclaredType returnType, 
			ConfigurationContext configurationContext);
	
	List<ConfigurationTypeElement> getAvailableConfigurations();
}