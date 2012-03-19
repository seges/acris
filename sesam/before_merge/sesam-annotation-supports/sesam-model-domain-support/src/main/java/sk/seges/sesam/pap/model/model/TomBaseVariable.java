package sk.seges.sesam.pap.model.model;

import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;

import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableVariable;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.pap.model.provider.RoundEnvConfigurationProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

abstract class TomBaseVariable extends DelegateMutableVariable {

	protected final TransferObjectProcessingEnvironment processingEnv;
	protected final RoundEnvironment roundEnv;

	protected final TransferObjectHelper toHelper;
	protected final TypeParametersSupport typeParametersSupport;

	private TomConfigurationHolderDelegate tomConfigurationHolderDelegate;

	private boolean configurationTypeInitialized = false;
	private List<ConfigurationTypeElement> configurationTypeElements;

	protected final ConfigurationProvider[] configurationProviders;

	protected TomBaseVariable(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationTypeElement[] configurationTypeElements, ConfigurationProvider... configurationProviders) {
		this.roundEnv = roundEnv;
		this.processingEnv = processingEnv;

		this.configurationProviders = configurationProviders;
		
		this.configurationTypeElements = Arrays.asList(configurationTypeElements);
		this.configurationTypeInitialized = true;
		
		this.toHelper = new TransferObjectHelper(processingEnv);
		this.typeParametersSupport = new TypeParametersSupport(processingEnv);
	}

	protected TomBaseVariable(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		this.roundEnv = roundEnv;
		this.processingEnv = processingEnv;

		this.configurationProviders = configurationProviders;
		
		this.toHelper = new TransferObjectHelper(processingEnv);
		this.typeParametersSupport = new TypeParametersSupport(processingEnv);
	}
	
	private TomConfigurationHolderDelegate ensureTomConfigurationHolderDelegate() {
		if (this.tomConfigurationHolderDelegate == null) {
			this.tomConfigurationHolderDelegate = new TomConfigurationHolderDelegate(getConfigurations());
		}
		
		return this.tomConfigurationHolderDelegate;
	}

	public List<ConfigurationTypeElement> getConfigurations() {
		if (!configurationTypeInitialized) {
			this.configurationTypeElements = getConfigurationsForType();
			this.configurationTypeInitialized = true;
		}
		return configurationTypeElements;
	}

	protected abstract List<ConfigurationTypeElement> getConfigurationsForType();
	
	public ConfigurationTypeElement getDomainDefinitionConfiguration() {
		return ensureTomConfigurationHolderDelegate().getDomainDefinitionConfiguration();
	}

	protected ConfigurationTypeElement getDtoDefinitionConfiguration() {
		return ensureTomConfigurationHolderDelegate().getDtoDefinitionConfiguration();
	}

	protected ConfigurationTypeElement getConverterDefinitionConfiguration() {
		return ensureTomConfigurationHolderDelegate().getConverterDefinitionConfiguration();
	}

	protected MutableTypes getMutableTypesUtils() {
		return processingEnv.getTypeUtils();
	}
	
	protected ConfigurationProvider[] getConfigurationProviders(ConfigurationProvider[] configurationProviders) {
		if (configurationProviders != null && configurationProviders.length > 0) {
			return configurationProviders;
		}

		ConfigurationProvider[] result = new ConfigurationProvider[1];
		result[0] = new RoundEnvConfigurationProvider(processingEnv, roundEnv);
		
		return result;
	}
}