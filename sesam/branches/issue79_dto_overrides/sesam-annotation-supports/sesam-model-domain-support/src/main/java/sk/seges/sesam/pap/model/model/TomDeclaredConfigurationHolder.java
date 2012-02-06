package sk.seges.sesam.pap.model.model;

import java.util.List;

import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public abstract class TomDeclaredConfigurationHolder extends TomBaseDeclaredType {

	private ConfigurationContext configurationContext;
	
	protected TomDeclaredConfigurationHolder(EnvironmentContext<TransferObjectProcessingEnvironment> environmentContext, ConfigurationContext configurationContext) {
		super(environmentContext);
		this.configurationContext = configurationContext;
	}

	protected ConfigurationContext ensureConfigurationContext() {
		if (configurationContext == null) {
			configurationContext = new ConfigurationContext(environmentContext.getConfigurationEnv());
			configurationContext.setConfigurations(getConfigurationsForType());
		}
		
		return configurationContext;
	}
	
	public ConfigurationProvider[] getConfigurationProviders() {
		return environmentContext.getConfigurationEnv().getConfigurationProviders();
	};
	
	public ConfigurationTypeElement getDomainDefinitionConfiguration() {
		return ensureConfigurationContext().getDomainDefinitionConfiguration();
	}

	protected ConfigurationTypeElement getDtoDefinitionConfiguration() {
		return ensureConfigurationContext().getDtoDefinitionConfiguration();
	}

	protected ConfigurationTypeElement getConverterDefinitionConfiguration() {
		return ensureConfigurationContext().getConverterDefinitionConfiguration();
	}

	public List<ConfigurationTypeElement> getConfigurations() {
		return ensureConfigurationContext().getConfigurations();
	}

	protected abstract List<ConfigurationTypeElement> getConfigurationsForType();	
}