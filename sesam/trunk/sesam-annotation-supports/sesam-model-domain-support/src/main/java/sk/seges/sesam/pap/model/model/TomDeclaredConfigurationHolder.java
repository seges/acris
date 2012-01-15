package sk.seges.sesam.pap.model.model;

import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;

import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public abstract class TomDeclaredConfigurationHolder extends TomBaseDeclaredType {

	private boolean configurationTypeInitialized = false;

	private final ConfigurationProvider[] configurationProviders;
	private List<ConfigurationTypeElement> configurationTypeElements;
	private TomConfigurationHolderDelegate tomConfigurationHolderDelegate;

	protected TomDeclaredConfigurationHolder(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationTypeElement[] configurationTypeElements, ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		this.configurationProviders = getConfigurationProviders(configurationProviders);
		this.configurationTypeElements = Arrays.asList(configurationTypeElements);
		this.configurationTypeInitialized = true;
	}

	private TomConfigurationHolderDelegate ensureTomConfigurationHolderDelegate() {
		if (this.tomConfigurationHolderDelegate == null) {
			this.tomConfigurationHolderDelegate = new TomConfigurationHolderDelegate(getConfigurations());
		}
		
		return this.tomConfigurationHolderDelegate;
	}
	
	protected TomDeclaredConfigurationHolder(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		this.configurationProviders = getConfigurationProviders(configurationProviders);
		this.configurationTypeInitialized = false;
	}

	public ConfigurationProvider[] getConfigurationProviders() {
		return configurationProviders;
	};
	
	public ConfigurationTypeElement getDomainDefinitionConfiguration() {
		return ensureTomConfigurationHolderDelegate().getDomainDefinitionConfiguration();
	}

	protected ConfigurationTypeElement getDtoDefinitionConfiguration() {
		return ensureTomConfigurationHolderDelegate().getDtoDefinitionConfiguration();
	}

	protected ConfigurationTypeElement getConverterDefinitionConfiguration() {
		return ensureTomConfigurationHolderDelegate().getConverterDefinitionConfiguration();
	}

	public List<ConfigurationTypeElement> getConfigurations() {
		if (!configurationTypeInitialized) {
			this.configurationTypeElements = getConfigurationsForType();
			this.configurationTypeInitialized = true;
		}
		return configurationTypeElements;
	}

	protected abstract List<ConfigurationTypeElement> getConfigurationsForType();	
}