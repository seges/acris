package sk.seges.sesam.pap.model.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;

import sk.seges.sesam.core.model.converter.CollectionConfiguration;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.ConfigurationContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class RoundEnvConfigurationProvider implements ConfigurationProvider {

	protected final EnvironmentContext<TransferObjectProcessingEnvironment> envContext;
	
	public RoundEnvConfigurationProvider(EnvironmentContext<TransferObjectProcessingEnvironment> envContext) {
		this.envContext = envContext;
	}
	
	protected Class<?>[] getCommonConfigurations() {
		return new Class<?> [] {
				CollectionConfiguration.class
		};
	}

	protected boolean isSupportedType(MutableTypeMirror type) {
		return (!type.getKind().equals(MutableTypeKind.PRIMITIVE));
	}
	
	protected Set<? extends Element> getConfigurationElements() {
		return envContext.getRoundEnv().getElementsAnnotatedWith(TransferObjectMapping.class);
	}

	public class ConfigurationComparator implements Comparator<ConfigurationTypeElement> {

		@Override
		public int compare(ConfigurationTypeElement o1, ConfigurationTypeElement o2) {
			ConfigurationTypeElement delegateConfigurationTypeElement1 = o1.getDelegateConfigurationTypeElement();
			ConfigurationTypeElement delegateConfigurationTypeElement2 = o2.getDelegateConfigurationTypeElement();
			
			if (delegateConfigurationTypeElement1 == null) {
				return -1;
			}
			
			if (delegateConfigurationTypeElement2 == null) {
				return 1;
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
		};
		
		abstract public boolean appliesForType(MutableTypeMirror type, ConfigurationTypeElement configurationTypeElement);
		abstract public ConfigurationTypeElement getConfiguration(MutableTypeMirror type, Element configurationElement, RoundEnvConfigurationProvider configurationProvider, ConfigurationContext context);
		abstract public List<ConfigurationTypeElement> retrieveConfigurations(MutableTypeMirror type, ConfigurationCache cache);
		abstract public void storeConfigurations(MutableTypeMirror type, List<ConfigurationTypeElement> configurations, ConfigurationCache cache);
	}
	
	public final List<ConfigurationTypeElement> getConfigurationsForDomain(MutableTypeMirror domainType) {
		return getConfigurationElementsForType(TargetType.DOMAIN, domainType);
	}

	public final List<ConfigurationTypeElement> getConfigurationsForDto(MutableTypeMirror domainType) {
		return getConfigurationElementsForType(TargetType.DTO, domainType);
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

		List<ConfigurationTypeElement> cachedConfigurations = targetType.retrieveConfigurations(type, envContext.getConfigurationEnv().getCache());
		
		if (cachedConfigurations != null) {
			return cachedConfigurations;
		}

		ConfigurationContext configurationContext = new ConfigurationContext(envContext.getConfigurationEnv());
		List<ConfigurationTypeElement> result = getConfigurationElementsForType(targetType, type, configurationContext);
		configurationContext.setConfigurations(result);
		
		Collections.sort(result, new ConfigurationComparator());

		targetType.storeConfigurations(type, result, envContext.getConfigurationEnv().getCache());

		return result;
	}
	
	protected List<ConfigurationTypeElement> getConfigurationElementsForType(TargetType targetType, MutableTypeMirror type, ConfigurationContext context) {
		
		List<ConfigurationTypeElement> result = new ArrayList<ConfigurationTypeElement>();

		if (!isSupportedType(type)) {
			return result;
		};
				
		Set<? extends Element> elementsAnnotatedWith = getConfigurationElements();
		for (Element annotatedElement : elementsAnnotatedWith) {
			if (annotatedElement.asType().getKind().equals(TypeKind.DECLARED) && !contains(annotatedElement, result)) {
				ConfigurationTypeElement configurationTypeElement = getConfigurationElement((TypeElement)annotatedElement);
				if (targetType.appliesForType(type, configurationTypeElement)) {
					result.add(targetType.getConfiguration(type, annotatedElement, this, context));
				}
			}
		}

		if (getCommonConfigurations() != null) {
			for (Class<?> clazz: getCommonConfigurations()) {
				TypeElement configurationElement = envContext.getProcessingEnv().getElementUtils().getTypeElement(clazz.getCanonicalName());
				if (configurationElement.getAnnotation(TransferObjectMapping.class) != null) {
					ConfigurationTypeElement configurationTypeElement = getConfigurationElement(configurationElement);
					if (targetType.appliesForType(type, configurationTypeElement)) {
						result.add(targetType.getConfiguration(type, configurationElement, this, context));
					}
				}
			}
		}
				
		return result;
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