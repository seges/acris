package sk.seges.sesam.pap.service.provider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.provider.RoundEnvConfigurationProvider;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class ServiceCollectorConfigurationProvider extends RoundEnvConfigurationProvider {

	protected final ServiceTypeElement service;
	private List<ConfigurationTypeElement> configurations = null;
	
	public ServiceCollectorConfigurationProvider(ServiceTypeElement service, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		this.service = service;
	}

	protected List<ConfigurationTypeElement> getConfigurationsFromType(TypeElement element, List<String> processedElements) {
		
		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();

		if (processedElements.contains(element.getQualifiedName().toString()	)) {
			return result;
		}

		processedElements.add(element.getQualifiedName().toString());
		
		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(element, processingEnv, roundEnv);
		if (configurationTypeElement.isValid()) {
			result.add(configurationTypeElement);
		}
		
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());
		
		for (ExecutableElement method: methods) {
			result.addAll(getConfigurationsFromMethod(method, processedElements));
		}

		List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());

		for (VariableElement variableElement: fields) {
			if (variableElement.asType().getKind().equals(TypeKind.DECLARED)) {
				result.addAll(getConfigurationsFromType((TypeElement)((DeclaredType)variableElement.asType()).asElement(), processedElements));
			}
		}
		
		if (element.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			result.addAll(getConfigurationsFromType((TypeElement)((DeclaredType)element.getSuperclass()).asElement(), processedElements));
		}
		
		return result;
	}
	
	protected List<ConfigurationTypeElement> getConfigurationsFromMethod(ExecutableElement method, List<String> processedElements) {
		
		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();
		
		if (method.getReturnType().getKind().equals(TypeKind.DECLARED)) {
			result.addAll(getConfigurationsFromType((TypeElement)((DeclaredType)method.getReturnType()).asElement(), processedElements));
		}
		
		for (VariableElement parameter: method.getParameters()) {
			if (parameter.asType().getKind().equals(TypeKind.DECLARED)) {
				result.addAll(getConfigurationsFromType((TypeElement)((DeclaredType)parameter.asType()).asElement(), processedElements));
			}
		}
		
		return result;
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
				result.addAll(getConfigurationsFromType(remoteServiceInterface.asElement(), processedElements));
			}
		}

		this.configurations = result;

		return result;
	}
	
	@Override
	public ConfigurationTypeElement getConfigurationForDomain(TypeMirror domainType) {
		if (!isSupportedType(domainType)) {
			return null;
		};

		for (ConfigurationTypeElement configurationTypeElement : collectConfigurations()) {
			if (configurationTypeElement.appliesForDomainType(domainType)) {
				return new ConfigurationTypeElement((DeclaredType)domainType, null, (TypeElement)configurationTypeElement.asElement(), processingEnv, roundEnv);
			}
		}

		return super.getConfigurationForDomain(domainType);
	}

	@Override
	public ConfigurationTypeElement getConfigurationForDto(TypeMirror dtoType) {
		if (!isSupportedType(dtoType)) {
			return null;
		};

		for (ConfigurationTypeElement configurationTypeElement : collectConfigurations()) {
			if (configurationTypeElement.appliesForDtoType(dtoType)) {
				return new ConfigurationTypeElement(null, (DeclaredType)dtoType, (TypeElement)configurationTypeElement.asElement(), processingEnv, roundEnv);
			}
		}

		return super.getConfigurationForDto(dtoType);
	}
}