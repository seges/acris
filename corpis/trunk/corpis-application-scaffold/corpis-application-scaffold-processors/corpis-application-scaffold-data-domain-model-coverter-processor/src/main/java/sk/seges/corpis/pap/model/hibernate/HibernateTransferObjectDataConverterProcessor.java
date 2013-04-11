package sk.seges.corpis.pap.model.hibernate;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import sk.seges.corpis.appscaffold.model.pap.model.DataConfigurationTypeElement;
import sk.seges.corpis.pap.model.hibernate.provider.DataClasspathConfigurationProvider;
import sk.seges.sesam.pap.model.model.ConfigurationContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateTransferObjectDataConverterProcessor extends HibernateTransferObjectConverterProcessor {

	protected ConfigurationTypeElement getConfigurationTypeElement(TypeElement typeElement) {
		ConfigurationContext configurationContext = new ConfigurationContext(environmentContext.getConfigurationEnv());
		ConfigurationTypeElement configurationTypeElement = new DataConfigurationTypeElement(typeElement, getEnvironmentContext(), configurationContext);
		configurationContext.addConfiguration(configurationTypeElement);
		
		return configurationTypeElement;
	}

	@Override
	protected ConfigurationProvider[] getConfigurationProviders() {
		return new ConfigurationProvider[] {
				new DataClasspathConfigurationProvider(getClassPathTypes(), getEnvironmentContext())
		};
	}
}