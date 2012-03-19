package sk.seges.sesam.pap.model.model;

import java.util.List;

public class TomConfigurationHolderDelegate {

	private final List<ConfigurationTypeElement> configurationTypeElements;
	
	TomConfigurationHolderDelegate(List<ConfigurationTypeElement> configurationTypeElements) {
		this.configurationTypeElements = configurationTypeElements;
	}

	ConfigurationTypeElement ensureConfiguration(TomType tomType) {
		ConfigurationTypeElement configuration = getConfiguration(tomType);
		
		if (configuration != null) {
			return configuration;
		}
		
		if (configurationTypeElements.size() == 0) {
			return null;
		}
		
		return configurationTypeElements.get(0);
	}
	
	ConfigurationTypeElement getConfiguration(TomType tomType) {
		for (ConfigurationTypeElement configuration: configurationTypeElements) {
			if (tomType.appliesFor(configuration)) {
				return configuration;
			}
		}

		return null;
	}

	ConfigurationTypeElement getDomainDefinitionConfiguration() {
		return ensureConfiguration(TomType.DOMAIN);
	}

	ConfigurationTypeElement getDtoDefinitionConfiguration() {
		return ensureConfiguration(TomType.DTO_DEFINED);
	}

	ConfigurationTypeElement getConverterDefinitionConfiguration() {
		ConfigurationTypeElement configuration = getConfiguration(TomType.CONVERTER_DEFINED);
		if (configuration != null) {
			return configuration;
		}
		return getConfiguration(TomType.CONVERTER_GENERATED);
	}
}