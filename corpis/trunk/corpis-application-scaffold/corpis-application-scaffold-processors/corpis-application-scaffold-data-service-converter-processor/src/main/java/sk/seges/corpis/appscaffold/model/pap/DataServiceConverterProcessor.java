package sk.seges.corpis.appscaffold.model.pap;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.appscaffold.model.pap.provider.DataServiceCollectorConfigurationProvider;
import sk.seges.corpis.pap.service.hibernate.HibernateServiceConverterProcessor;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DataServiceConverterProcessor extends HibernateServiceConverterProcessor {

	@Override
	protected ConfigurationProvider[] getConfigurationProviders(ServiceTypeElement service, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
		return new ConfigurationProvider[] {
				new DataServiceCollectorConfigurationProvider(service, getClassPathTypes(), context)
		};
	}
}