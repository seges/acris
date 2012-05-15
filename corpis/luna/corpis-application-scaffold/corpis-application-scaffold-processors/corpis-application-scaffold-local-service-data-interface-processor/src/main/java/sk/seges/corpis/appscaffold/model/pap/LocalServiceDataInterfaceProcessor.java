package sk.seges.corpis.appscaffold.model.pap;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.appscaffold.model.pap.provider.DomainDataConfigurationProvider;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.service.ServiceInterfaceProcessor;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class LocalServiceDataInterfaceProcessor extends ServiceInterfaceProcessor {

	@Override
	protected ConfigurationProvider[] getConfigurationProviders(RemoteServiceTypeElement remoteServiceInterfaceElement, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
		return new ConfigurationProvider[] {
				new DomainDataConfigurationProvider(getClassPathTypes(), environmentContext)
		};
	}
}
