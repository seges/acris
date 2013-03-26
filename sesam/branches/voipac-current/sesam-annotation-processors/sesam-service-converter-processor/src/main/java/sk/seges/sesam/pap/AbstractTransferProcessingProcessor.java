package sk.seges.sesam.pap;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.pap.model.model.ConfigurationEnvironment;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.ConfigurationCache;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public abstract class AbstractTransferProcessingProcessor extends MutableAnnotationProcessor {

	protected TransferObjectProcessingEnvironment processingEnv;
	protected EnvironmentContext<TransferObjectProcessingEnvironment> environmentContext;
	protected ConfigurationProvider[] configurationProviders = null;

	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	protected ConfigurationCache getConfigurationCache() {
		return new ConfigurationCache();
	}
	
	protected ConfigurationProvider[] ensureConfigurationProviders(MutableDeclaredType mutableType, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
		if (configurationProviders == null) {
			configurationProviders = getConfigurationProviders(mutableType, context);
		}
		
		return configurationProviders;
	}

	protected abstract ConfigurationProvider[] getConfigurationProviders(MutableDeclaredType mutableType, EnvironmentContext<TransferObjectProcessingEnvironment> context);

	protected MutableDeclaredType getTargetType(TypeElement element) {
		return null;
	}
	
	@Override
	protected void init(Element element, RoundEnvironment roundEnv) {
		super.init(element, roundEnv);
		this.processingEnv = new TransferObjectProcessingEnvironment(super.processingEnv, roundEnv, getConfigurationCache());
		
		MutableDeclaredType mutableType = getTargetType((TypeElement)element);
		EnvironmentContext<TransferObjectProcessingEnvironment> context = getEnvironmentContext(mutableType);
		this.processingEnv.setConfigurationProviders(ensureConfigurationProviders(mutableType, context));
	}

	protected EnvironmentContext<TransferObjectProcessingEnvironment> getEnvironmentContext(MutableDeclaredType mutableType) {
		if (environmentContext == null) {
			ConfigurationEnvironment configurationEnv = new ConfigurationEnvironment(processingEnv, roundEnv, getConfigurationCache());
			environmentContext = configurationEnv.getEnvironmentContext();
			configurationEnv.setConfigurationProviders(ensureConfigurationProviders(mutableType, environmentContext));
		}
		
		return environmentContext;
	}
}