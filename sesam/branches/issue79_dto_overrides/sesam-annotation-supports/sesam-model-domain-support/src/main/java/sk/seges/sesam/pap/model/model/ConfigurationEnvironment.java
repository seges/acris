package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.provider.ConfigurationCache;
import sk.seges.sesam.pap.model.provider.RoundEnvConfigurationProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class ConfigurationEnvironment {

	private final ConfigurationCache cache;
	private ConfigurationProvider[] configurationProviders;

	private EnvironmentContext<TransferObjectProcessingEnvironment> environmentContext;
	private final TransferObjectProcessingEnvironment processingEnv;
	private final RoundEnvironment roundEnv;
	
	public ConfigurationEnvironment(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationCache cache) {
		this.cache = cache;
		this.roundEnv = roundEnv;
		this.processingEnv = processingEnv;
	}
	
	public void setConfigurationProviders(ConfigurationProvider... configurationProviders) {
		this.configurationProviders = getConfigurationProviders(configurationProviders, processingEnv, roundEnv, cache);
	}
	
	public ConfigurationCache getCache() {
		return cache;
	}

	public EnvironmentContext<TransferObjectProcessingEnvironment> getEnvironmentContext() {
		if (environmentContext == null) {
			environmentContext = new EnvironmentContext<TransferObjectProcessingEnvironment>(processingEnv, roundEnv, this);
		}
		
		return environmentContext;
	}
	
	public ConfigurationTypeElement getConfiguration(ExecutableElement configurationElementMethod, DomainDeclaredType returnType, 
			ConfigurationContext configurationContext) {
		if (configurationProviders == null) {
			return null;
		}
		
		for (ConfigurationProvider configurationProvider: configurationProviders) {
			ConfigurationTypeElement configuration = configurationProvider.getConfiguration(configurationElementMethod, returnType, configurationContext);
			
			if (configuration != null) {
				return configuration;
			}
		}
		
		return null;
	}
	
	protected ConfigurationProvider[] getConfigurationProviders(ConfigurationProvider[] configurationProviders, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationCache cache) {
		if (configurationProviders != null && configurationProviders.length > 0) {
			return configurationProviders;
		}

		ConfigurationProvider[] result = new ConfigurationProvider[1];
		result[0] = new RoundEnvConfigurationProvider(getEnvironmentContext());
		
		return result;
	}

	public ConfigurationProvider[] getConfigurationProviders() {
		return configurationProviders;
	}
}