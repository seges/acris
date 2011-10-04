package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.RoundEnvironment;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class TransferObjectProcessingEnvironment extends MutableProcessingEnvironment {

	private TransferObjectTypes types;
	private final RoundEnvironment roundEnv;
	private ConfigurationProvider[] configurationProviders;
	
	public TransferObjectProcessingEnvironment(MutableProcessingEnvironment processingEnvironment, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(processingEnvironment);
		this.roundEnv = roundEnv;
		this.configurationProviders = configurationProviders;
	}

	public void setConfigurationProviders(ConfigurationProvider... configurationProviders) {
		this.configurationProviders = configurationProviders;
	}
	
	public TransferObjectTypes getTransferObjectUtils() {
		if (types == null) {
			types = new TransferObjectTypes(this, roundEnv, configurationProviders);
		}
		return types;
	}
}