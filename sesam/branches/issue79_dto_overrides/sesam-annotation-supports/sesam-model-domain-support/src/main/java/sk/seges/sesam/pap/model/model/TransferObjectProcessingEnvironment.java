package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.RoundEnvironment;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.ConfigurationCache;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class TransferObjectProcessingEnvironment extends MutableProcessingEnvironment {

	private TransferObjectTypes types;
	private final RoundEnvironment roundEnv;
	private final ConfigurationCache configurationCache;

	private ConfigurationProvider[] configurationProviders;
	
	private EnvironmentContext<TransferObjectProcessingEnvironment> envContext = null;
	
	public TransferObjectProcessingEnvironment(MutableProcessingEnvironment processingEnvironment, RoundEnvironment roundEnv, ConfigurationCache configurationCache) {
		super(processingEnvironment);
		this.roundEnv = roundEnv;
		this.configurationCache = configurationCache;
	}

	public void setConfigurationProviders(ConfigurationProvider... configurationProviders) {
		this.configurationProviders = configurationProviders;
	}

	public EnvironmentContext<TransferObjectProcessingEnvironment> getEnvironmentContext() {
		if (envContext == null) {
			ConfigurationEnvironment configurationEnvironment = new ConfigurationEnvironment(this, roundEnv, configurationCache);
			configurationEnvironment.setConfigurationProviders(configurationProviders);
			envContext = configurationEnvironment.getEnvironmentContext();
		}
		
		return this.envContext;
	}
	
	public TransferObjectTypes getTransferObjectUtils() {
		if (types == null) {
			types = new TransferObjectTypes(getEnvironmentContext());
		}
		return types;
	}
}