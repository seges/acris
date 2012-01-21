package sk.seges.sesam.pap.service.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;

public abstract class AbstractServiceCollectorConfigurationProvider extends ClasspathConfigurationProvider {

	public AbstractServiceCollectorConfigurationProvider(ClassPathTypes classpathUtils,
			TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(classpathUtils, processingEnv, roundEnv);
	}

	protected List<ConfigurationTypeElement> getConfigurationsFromType(DeclaredType type, List<String> processedElements) {

		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();

		if (type.getTypeArguments().size() > 0) {
			for (TypeMirror typeArgument: type.getTypeArguments()) {
				if (typeArgument.getKind().equals(TypeKind.DECLARED)) {
					result.addAll(getConfigurationsFromType((DeclaredType)typeArgument, processedElements));
				}
			}
		}

		TypeElement element = (TypeElement)type.asElement();
		
		if (processingEnv.getElementUtils().getPackageOf(element).toString().startsWith("java.")) {
			return result;
		}

		if (processedElements.contains(element.getQualifiedName().toString())) {
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
				result.addAll(getConfigurationsFromType((DeclaredType)variableElement.asType(), processedElements));
			}
		}
		
		if (element.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			result.addAll(getConfigurationsFromType((DeclaredType)element.getSuperclass(), processedElements));
		}
		
		return result;
	}

	protected List<ConfigurationTypeElement> getConfigurationsFromMethod(ExecutableElement method, List<String> processedElements) {
		
		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();
		
		if (method.getReturnType().getKind().equals(TypeKind.DECLARED)) {
			result.addAll(getConfigurationsFromType((DeclaredType)method.getReturnType(), processedElements));
		}
		
		for (VariableElement parameter: method.getParameters()) {
			if (parameter.asType().getKind().equals(TypeKind.DECLARED)) {
				result.addAll(getConfigurationsFromType((DeclaredType)parameter.asType(), processedElements));
			}
		}
		
		return result;
	}
	
	protected abstract List<ConfigurationTypeElement> collectConfigurations();

	@Override
	public List<ConfigurationTypeElement> getConfigurationsForDto(MutableTypeMirror dtoType) {

		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();
		
		if (!isSupportedType(dtoType)) {
			return result;
		};

		for (ConfigurationTypeElement configurationTypeElement : collectConfigurations()) {
			if (configurationTypeElement.appliesForDtoType(dtoType)) {
				//result = configurationTypeElement;

//				if (result.getDelegateConfigurationTypeElement() == null) {
					result.add(getConfigurationElement(null, (MutableDeclaredType)dtoType, (TypeElement)configurationTypeElement.asConfigurationElement()));
					//return new ConfigurationTypeElement(null, (MutableDeclaredType)dtoType, (TypeElement)result.asConfigurationElement(), processingEnv, roundEnv);
//				}
			}
		}
		
		result.addAll(super.getConfigurationsForDto(dtoType));

		Collections.sort(result, new ConfigurationComparator());

//		if (result.size() > 0) {
			return result;
//		}

//		if (result != null) {
//			return getConfigurationElement(null, (MutableDeclaredType)dtoType, (TypeElement)result.asConfigurationElement());
//			//return new ConfigurationTypeElement(null, (MutableDeclaredType)dtoType, (TypeElement)result.asConfigurationElement(), processingEnv, roundEnv);
//		}

//		return super.getConfigurationsForDto(dtoType);
	}

	@Override
	public List<ConfigurationTypeElement> getConfigurationsForDomain(MutableTypeMirror domainType) {
		
		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();
				
		if (!isSupportedType(domainType)) {
			return result;
		};

		for (ConfigurationTypeElement configurationTypeElement : collectConfigurations()) {
			if (configurationTypeElement.appliesForDomainType(domainType)) {
//				result = configurationTypeElement;
				result.add(getConfigurationElement(domainType, null, (TypeElement)configurationTypeElement.asConfigurationElement()));

//				if (result.getDelegateConfigurationTypeElement() == null) {
//					return getConfigurationElement(domainType, null, (TypeElement)result.asConfigurationElement());
//					//return new ConfigurationTypeElement((MutableDeclaredType)domainType, null, (TypeElement)result.asConfigurationElement(), processingEnv, roundEnv);
//				}
			}
		}

		result.addAll(super.getConfigurationsForDomain(domainType));

		Collections.sort(result, new ConfigurationComparator());
				
//		if (result != null) {
//			return getConfigurationElement((MutableDeclaredType)domainType, null, (TypeElement)result.asConfigurationElement());
//			//return new ConfigurationTypeElement((MutableDeclaredType)domainType, null, (TypeElement)result.asConfigurationElement(), processingEnv, roundEnv);
//		}
		
		return result;
	}
}