package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.pap.model.provider.RoundEnvConfigurationProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

public class TomBase {

	protected final TransferObjectHelper toHelper;
	protected final ProcessingEnvironment processingEnv;
	private final NameTypesUtils nameTypesUtils;
	protected final RoundEnvironment roundEnv;
	
	protected TomBase(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		this.roundEnv = roundEnv;
		this.processingEnv = processingEnv;

		this.nameTypesUtils = new NameTypesUtils(processingEnv);
		this.toHelper = new TransferObjectHelper(nameTypesUtils, processingEnv, roundEnv, new MethodHelper(processingEnv, nameTypesUtils));
	}
	
	protected NameTypesUtils getNameTypesUtils() {
		return nameTypesUtils;
	}

	protected ConfigurationProvider[] getConfigurationProviders(ConfigurationProvider[] configurationProviders) {
		if (configurationProviders != null) {
			return configurationProviders;
		}

		ConfigurationProvider[] result = new ConfigurationProvider[1];
		result[0] = new RoundEnvConfigurationProvider(processingEnv, roundEnv);
		
		return result;
	}
}
