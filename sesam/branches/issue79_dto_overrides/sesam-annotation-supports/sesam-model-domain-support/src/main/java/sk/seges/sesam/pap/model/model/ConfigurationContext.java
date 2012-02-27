package sk.seges.sesam.pap.model.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigurationContext {

	private List<ConfigurationTypeElement> configurations = new ArrayList<ConfigurationTypeElement>();
	private final ConfigurationEnvironment env;
	
	public ConfigurationContext(ConfigurationEnvironment env) {
		this.env = env;
	}

	public ConfigurationContext addConfiguration(ConfigurationTypeElement configuration) {
		configurations.add(configuration);
		return this;
	}
	
	public void setConfigurations(List<ConfigurationTypeElement> configurations) {
		this.configurations = configurations;
	}
	
	public ConfigurationEnvironment getEnv() {
		return env;
	}
	
	public List<ConfigurationTypeElement> getConfigurations() {
		return Collections.unmodifiableList(configurations);
	}
		
	ConfigurationTypeElement ensureConfiguration(TomType... tomTypes) {
		ConfigurationTypeElement configuration = getConfiguration(tomTypes);
		
		if (configuration != null) {
			return configuration;
		}
		
		if (configurations.size() == 0) {
			return null;
		}
		
		return configurations.get(0);
	}
	
	ConfigurationTypeElement getConfiguration(TomType... tomTypes) {
		for (ConfigurationTypeElement configuration: configurations) {
			if (TomType.appliesFor(configuration, tomTypes)) {
				return configuration;
			}
		}

		return null;
	}

	public ConfigurationTypeElement getDelegateDomainDefinitionConfiguration() {
		ConfigurationTypeElement domainDefinitionConfiguration = getDomainDefinitionConfiguration();

		if (domainDefinitionConfiguration != null && domainDefinitionConfiguration.getDelegateConfigurationTypeElement() != null) {
			domainDefinitionConfiguration = domainDefinitionConfiguration.getDelegateConfigurationTypeElement();
		}
		
		return domainDefinitionConfiguration;
	}

	ConfigurationTypeElement getDomainDefinitionConfiguration() {
		ConfigurationTypeElement configuration = getConfiguration(TomType.DOMAIN, TomType.DTO_NOT_DEFINED);
		if (configuration != null) {
			return configuration;
		}
		return ensureConfiguration(TomType.DOMAIN);
	}

//	ConfigurationTypeElement getDtoDefinitionConfiguration() {
//		return ensureConfiguration(TomType.DTO_DEFINED);
//	}

	ConfigurationTypeElement getConverterDefinitionConfiguration() {
		ConfigurationTypeElement configuration = getConfiguration(TomType.CONVERTER_DEFINED);
		if (configuration != null) {
			return configuration;
		}
		return getConfiguration(TomType.CONVERTER_GENERATED);
	}
}