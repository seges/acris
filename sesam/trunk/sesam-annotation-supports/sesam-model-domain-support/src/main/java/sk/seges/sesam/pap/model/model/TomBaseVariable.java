package sk.seges.sesam.pap.model.model;

import java.util.List;

import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableVariable;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

abstract class TomBaseVariable extends DelegateMutableVariable {

	protected final EnvironmentContext<TransferObjectProcessingEnvironment> envContext;
	protected ConfigurationContext configurationContext;
	
	protected final TypeParametersSupport typeParametersSupport;
	
	protected TomBaseVariable(EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		this.envContext = envContext;
		this.configurationContext = configurationContext;
		this.typeParametersSupport = new TypeParametersSupport(envContext.getProcessingEnv());
	}
	
	private ConfigurationContext ensureConfigurationContext() {
		if (configurationContext == null) {
			configurationContext = new ConfigurationContext(envContext.getConfigurationEnv());
			configurationContext.setConfigurations(getConfigurationsForType());
		}
		
		return configurationContext;
	}

	protected MutableTypes getMutableTypesUtils() {
		return envContext.getProcessingEnv().getTypeUtils();
	}
	
	protected MutableTypes getTypeUtils() {
		return envContext.getProcessingEnv().getTypeUtils();
	}

	protected TransferObjectTypes getTransferObjectUtils() {
		return envContext.getProcessingEnv().getTransferObjectUtils();
	}
	
	public ConfigurationProvider[] getConfigurationProviders() {
		return envContext.getConfigurationEnv().getConfigurationProviders();
	};
	
	public ConfigurationTypeElement getDomainDefinitionConfiguration() {
		return ensureConfigurationContext().getDomainDefinitionConfiguration();
	}

//	protected ConfigurationTypeElement getDtoDefinitionConfiguration() {
//		return ensureConfigurationContext().getDtoDefinitionConfiguration();
//	}

	protected ConfigurationTypeElement getConverterDefinitionConfiguration() {
		return ensureConfigurationContext().getConverterDefinitionConfiguration();
	}

	public List<ConfigurationTypeElement> getConfigurations() {
		return ensureConfigurationContext().getConfigurations();
	}

	protected abstract List<ConfigurationTypeElement> getConfigurationsForType();
	
}