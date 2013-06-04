package sk.seges.corpis.pap.model.hibernate.provider;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import sk.seges.corpis.appscaffold.model.pap.model.DataConfigurationTypeElement;
import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;

public class DataClasspathConfigurationProvider extends ClasspathConfigurationProvider {

	public DataClasspathConfigurationProvider(ClassPathTypes classpathUtils, EnvironmentContext<TransferObjectProcessingEnvironment> envContext) {
		super(classpathUtils, envContext);
	}

	@Override
	public ConfigurationTypeElement getConfiguration(ExecutableElement configurationElementMethod, DomainDeclaredType returnType, ConfigurationContext configurationContext) {
		return new DataConfigurationTypeElement(configurationElementMethod, returnType, envContext, configurationContext);
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

	@Override
	protected List<ConfigurationTypeElement> getConfigurationElementsForType(TargetType targetType, MutableTypeMirror type, ConfigurationContext context) {
		List<ConfigurationTypeElement> configurationElementsForType = super.getConfigurationElementsForType(targetType, type, context);
		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();
		
		for (ConfigurationTypeElement configurationElement: configurationElementsForType) {
			if (configurationElement.getInstantiableDomainSpecified() != null && 
				configurationElement.getInstantiableDomainSpecified().getQualifiedName().toString().equals(type.toString(ClassSerializer.QUALIFIED, false))) {
				//if this is configuration exactly for this type, use it
				result.add(configurationElement);
			}
		}
		
		if (result.size() > 0) {
			return result;
		}
		
		return configurationElementsForType;
	}
}