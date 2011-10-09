package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.RoundEnvironment;

import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.pap.model.provider.RoundEnvConfigurationProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

abstract class TomBaseType extends DelegateMutableType {

	protected final TransferObjectProcessingEnvironment processingEnv;
	protected final RoundEnvironment roundEnv;

	protected final TransferObjectHelper toHelper;
	protected final TypeParametersSupport typeParametersSupport;
	
	protected TomBaseType(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		this.roundEnv = roundEnv;
		this.processingEnv = processingEnv;

		this.toHelper = new TransferObjectHelper(processingEnv);
		this.typeParametersSupport = new TypeParametersSupport(processingEnv);
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