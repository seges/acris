package sk.seges.sesam.pap.service.provider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class ServiceCollectorConfigurationProvider extends AbstractServiceCollectorConfigurationProvider {

	protected final ServiceTypeElement service;
	private List<ConfigurationTypeElement> configurations = null;
	
	public ServiceCollectorConfigurationProvider(ServiceTypeElement service, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		this.service = service;
	}
		
	protected List<ConfigurationTypeElement> collectConfigurations() {

		if (configurations != null) {
			return configurations;
		}

		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();

		List<LocalServiceTypeElement> localServiceInterfaces = service.getLocalServiceInterfaces();
		
		ArrayList<String> processedElements = new ArrayList<String>();
		
		for (LocalServiceTypeElement localService: localServiceInterfaces) {
			RemoteServiceTypeElement remoteServiceInterface = localService.getRemoteServiceInterface();
			if (remoteServiceInterface != null) {
				result.addAll(getConfigurationsFromType((DeclaredType)remoteServiceInterface.asElement().asType(), processedElements));
			}
		}

		this.configurations = result;

		return result;
	}
}