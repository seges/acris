package sk.seges.sesam.pap.model.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.ConfigurationContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class RoundEnvConfigurationProvider implements ConfigurationProvider {

	protected final EnvironmentContext<TransferObjectProcessingEnvironment> envContext;
	
	public RoundEnvConfigurationProvider(EnvironmentContext<TransferObjectProcessingEnvironment> envContext) {
		this.envContext = envContext;
	}
	
	protected boolean isSupportedType(MutableTypeMirror type) {
		return (!type.getKind().equals(MutableTypeKind.PRIMITIVE));
	}
	
	protected Set<? extends Element> getConfigurationElements() {
		return envContext.getRoundEnv().getElementsAnnotatedWith(TransferObjectMapping.class);
	}

	public class ConfigurationComparator implements Comparator<ConfigurationTypeElement> {

		private final String typeName;
		private final TargetType targetType;
		
		public ConfigurationComparator(TargetType targetType, MutableTypeMirror type) {
			this.targetType = targetType;
			if (type != null) {
				this.typeName = type.toString(ClassSerializer.CANONICAL, false);
			} else {
				this.typeName = null;
			}
		}
		
		@Override
		public int compare(ConfigurationTypeElement o1, ConfigurationTypeElement o2) {
			ConfigurationTypeElement delegateConfigurationTypeElement1 = o1.getDelegateConfigurationTypeElement();
			ConfigurationTypeElement delegateConfigurationTypeElement2 = o2.getDelegateConfigurationTypeElement();

			if (targetType != null) {
				TypeElement o1Type = targetType.getType(o1);
				
				if (o1Type != null && o1Type.getQualifiedName().toString().equals(typeName)) {
					return 5;
				}
	
				TypeElement o2Type = targetType.getType(o2);
	
				if (o2Type != null && o2Type.getQualifiedName().toString().equals(typeName)) {
					return -5;
				}
			}
			
			if (delegateConfigurationTypeElement1 == null) {
				if (!o1.hasInstantiableDomainSpecified()) {
					return 3;
				}
				return 2;
			}
			
			if (!o1.hasInstantiableDomainSpecified()) {
				return 1;
			}
			
			if (delegateConfigurationTypeElement2 == null) {
				if (!o2.hasInstantiableDomainSpecified()) {
					return -3;
				}
				return -2;
			}

			if (!o2.hasInstantiableDomainSpecified()) {
				return -1;
			}

			return 0;
		}
		
	}
	
	protected enum TargetType {
		DTO {
			@Override
			public boolean appliesForType(MutableTypeMirror type, ConfigurationTypeElement configurationTypeElement) {
				return configurationTypeElement.appliesForDtoType(type);
			}

			@Override
			public ConfigurationTypeElement getConfiguration(MutableTypeMirror type, Element configurationElement, RoundEnvConfigurationProvider configurationProvider, ConfigurationContext context) {
				return configurationProvider.getConfigurationElement(null, type, configurationElement, context);
			}

			@Override
			public List<ConfigurationTypeElement> retrieveConfigurations(MutableTypeMirror type, ConfigurationCache cache) {
				return cache.getConfigurationForDTO(type);
			}

			@Override
			public void storeConfigurations(MutableTypeMirror type, List<ConfigurationTypeElement> configurations,	ConfigurationCache cache) {
				cache.registerDto(type, configurations);
			}

			@Override
			public TypeElement getType(ConfigurationTypeElement configurationTypeElement) {
				return configurationTypeElement.getDtoSpecified();
			}
		}, DOMAIN {
			@Override
			public boolean appliesForType(MutableTypeMirror type, ConfigurationTypeElement configurationTypeElement) {
				return configurationTypeElement.appliesForDomainType(type);
			}

			@Override
			public ConfigurationTypeElement getConfiguration(MutableTypeMirror type, Element configurationElement, RoundEnvConfigurationProvider configurationProvider, ConfigurationContext context) {
				return configurationProvider.getConfigurationElement(type, null, configurationElement, context);
			}

			@Override
			public List<ConfigurationTypeElement> retrieveConfigurations(MutableTypeMirror type, ConfigurationCache cache) {
				return cache.getConfigurationForDomain(type);
			}

			@Override
			public void storeConfigurations(MutableTypeMirror type, List<ConfigurationTypeElement> configurations, ConfigurationCache cache) {
				cache.registerDomain(type, configurations);
			}

			@Override
			public TypeElement getType(ConfigurationTypeElement configurationTypeElement) {
				return configurationTypeElement.getInstantiableDomainSpecified();
			}
		};
		
		abstract public boolean appliesForType(MutableTypeMirror type, ConfigurationTypeElement configurationTypeElement);
		abstract public ConfigurationTypeElement getConfiguration(MutableTypeMirror type, Element configurationElement, RoundEnvConfigurationProvider configurationProvider, ConfigurationContext context);
		abstract public List<ConfigurationTypeElement> retrieveConfigurations(MutableTypeMirror type, ConfigurationCache cache);
		abstract public void storeConfigurations(MutableTypeMirror type, List<ConfigurationTypeElement> configurations, ConfigurationCache cache);
		abstract public TypeElement getType(ConfigurationTypeElement configurationTypeElement);
	}
	
	public final List<ConfigurationTypeElement> getConfigurationsForDomain(MutableTypeMirror domainType) {
		return getConfigurationElementsForType(TargetType.DOMAIN, domainType);
	}

	public final List<ConfigurationTypeElement> getConfigurationsForDto(MutableTypeMirror dtoType) {
		return getConfigurationElementsForType(TargetType.DTO, dtoType);
	}

	private boolean contains(Element element, List<ConfigurationTypeElement> configurations) {
		for (ConfigurationTypeElement configuration: configurations) {
			if (configuration.asConfigurationElement().equals(configuration)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected final List<ConfigurationTypeElement> getConfigurationElementsForType(TargetType targetType, MutableTypeMirror type) {

		List<ConfigurationTypeElement> cachedConfigurations = null;
		
		if (!type.getKind().isDeclared() || !((MutableDeclaredType)type).hasTypeParameters()) {
			 cachedConfigurations = targetType.retrieveConfigurations(type, envContext.getConfigurationEnv().getCache());
		}
		
		if (cachedConfigurations != null) {
			return cachedConfigurations;
		}

		ConfigurationContext configurationContext = new ConfigurationContext(envContext.getConfigurationEnv());
		List<ConfigurationTypeElement> result = getConfigurationElementsForType(targetType, type, configurationContext);
		configurationContext.setConfigurations(result);
		
		Collections.sort(result, new ConfigurationComparator(targetType, type));

		if (!type.getKind().isDeclared() || !((MutableDeclaredType)type).hasTypeParameters()) {
			targetType.storeConfigurations(type, result, envContext.getConfigurationEnv().getCache());
		}
		
		return result;
	}
	
	public List<ConfigurationTypeElement> getAvailableConfigurations() {
		
		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();

		Set<? extends Element> elementsAnnotatedWith = getConfigurationElements();
		for (Element annotatedElement : elementsAnnotatedWith) {
			if (annotatedElement.asType().getKind().equals(TypeKind.DECLARED) && !contains(annotatedElement, result)) {
				ConfigurationTypeElement configurationTypeElement = getConfigurationElement((TypeElement)annotatedElement);
				result.add(configurationTypeElement);
			}
		}
		
		return result;
	}

	protected List<ConfigurationTypeElement> getConfigurationElementsForType(TargetType targetType, MutableTypeMirror type, ConfigurationContext context) {
		
		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();

		if (!isSupportedType(type)) {
			return result;
		}
		
		Set<? extends Element> elementsAnnotatedWith = getConfigurationElements();
		for (Element annotatedElement : elementsAnnotatedWith) {
			if (annotatedElement.asType().getKind().equals(TypeKind.DECLARED) && !contains(annotatedElement, result)) {
				ConfigurationTypeElement configurationTypeElement = getConfigurationElement((TypeElement)annotatedElement);
				if (targetType.appliesForType(type, configurationTypeElement)) {
					result.add(targetType.getConfiguration(type, annotatedElement, this, context));
				}
			}
		}

		return result;
	}

	@Override
	public ConfigurationTypeElement getConfiguration(ExecutableElement configurationElementMethod, DomainDeclaredType returnType, ConfigurationContext configurationContext) {
		return new ConfigurationTypeElement(configurationElementMethod, returnType, envContext, configurationContext);
	}
	
	protected ConfigurationTypeElement getConfigurationElement(MutableTypeMirror domainType, MutableTypeMirror dtoType, Element annotatedElement, ConfigurationContext configurationContext) {
		return new ConfigurationTypeElement((MutableDeclaredType)domainType, (MutableDeclaredType)dtoType, (TypeElement)annotatedElement, envContext, configurationContext);
	}

	protected ConfigurationTypeElement getConfigurationElement(Element configurationElement) {
		ConfigurationContext configurationContext = new ConfigurationContext(envContext.getConfigurationEnv());
		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(configurationElement, envContext, configurationContext);
		configurationContext.addConfiguration(configurationTypeElement);
		
		return configurationTypeElement;
	}
}