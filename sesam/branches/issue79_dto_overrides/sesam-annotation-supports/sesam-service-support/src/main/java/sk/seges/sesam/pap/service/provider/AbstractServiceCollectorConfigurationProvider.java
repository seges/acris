package sk.seges.sesam.pap.service.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;

public abstract class AbstractServiceCollectorConfigurationProvider extends ClasspathConfigurationProvider {

	public AbstractServiceCollectorConfigurationProvider(ClassPathTypes classpathUtils, EnvironmentContext<TransferObjectProcessingEnvironment> envContext) {
		super(classpathUtils, envContext);
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
		
		if (envContext.getProcessingEnv().getElementUtils().getPackageOf(element).toString().startsWith("java.")) {
			return result;
		}

		if (processedElements.contains(element.getQualifiedName().toString())) {
			return result;
		}
		
		processedElements.add(element.getQualifiedName().toString());
		
		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(element, envContext, null);
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
		
		Collections.sort(result, new ConfigurationComparator());

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
		
		Collections.sort(result, new ConfigurationComparator());

		return result;
	}
	
	protected abstract List<ConfigurationTypeElement> collectConfigurations();

	@Override
	protected List<ConfigurationTypeElement> getConfigurationElementsForType(TargetType targetType, MutableTypeMirror type) {
		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();
		
		if (!isSupportedType(type)) {
			return result;
		};

		ConfigurationContext configurationContext = new ConfigurationContext(envContext.getConfigurationEnv());

		for (ConfigurationTypeElement configurationTypeElement : collectConfigurations()) {
			if (targetType.appliesForType(type, configurationTypeElement)) {
				result.add(targetType.getConfiguration(type, configurationTypeElement.asConfigurationElement(), this, configurationContext));
			}
		}
		
		result.addAll(super.getConfigurationElementsForType(targetType, type));

		Collections.sort(result, new ConfigurationComparator());

		return result;
	}	
}