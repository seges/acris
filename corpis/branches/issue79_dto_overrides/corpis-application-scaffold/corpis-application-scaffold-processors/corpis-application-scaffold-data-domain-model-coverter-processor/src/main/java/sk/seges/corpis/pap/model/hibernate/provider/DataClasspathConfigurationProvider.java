package sk.seges.corpis.pap.model.hibernate.provider;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import sk.seges.corpis.appscaffold.model.pap.model.DataConfigurationTypeElement;
import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;

public class DataClasspathConfigurationProvider extends ClasspathConfigurationProvider {

	public DataClasspathConfigurationProvider(ClassPathTypes classpathUtils, EnvironmentContext<TransferObjectProcessingEnvironment> envContext) {
		super(classpathUtils, envContext);
	}

	@Override
	protected ConfigurationTypeElement getConfigurationElement(MutableTypeMirror domainType, MutableTypeMirror dtoType,	Element annotatedElement, ConfigurationContext context) {
		return new DataConfigurationTypeElement((MutableDeclaredType)domainType, (MutableDeclaredType)dtoType, (TypeElement)annotatedElement, envContext, context);
	}

	@Override
	protected ConfigurationTypeElement getConfigurationElement(Element configurationElement) {
		ConfigurationContext configurationContext = new ConfigurationContext(envContext.getConfigurationEnv());
		ConfigurationTypeElement configurationTypeElement = new DataConfigurationTypeElement(configurationElement, envContext, configurationContext);
		configurationContext.addConfiguration(configurationTypeElement);
		
		return configurationTypeElement;
	}

}
