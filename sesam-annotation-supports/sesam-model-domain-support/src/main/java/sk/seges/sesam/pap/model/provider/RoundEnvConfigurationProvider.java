package sk.seges.sesam.pap.model.provider;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.model.converter.CollectionConfiguration;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectConfiguration;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class RoundEnvConfigurationProvider implements ConfigurationProvider {

	protected final ProcessingEnvironment processingEnv;
	protected final RoundEnvironment roundEnv;
	
	public RoundEnvConfigurationProvider(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
	}
	
	protected Class<?>[] getCommonConfigurations() {
		return new Class<?> [] {
				CollectionConfiguration.class
		};
	}

	protected boolean isSupportedType(TypeMirror type) {
		return (!type.getKind().isPrimitive() && !type.getKind().equals(TypeKind.NONE) && !type.getKind().equals(TypeKind.NULL) && !type.getKind().equals(TypeKind.ERROR));
	}
	
	public ConfigurationTypeElement getConfigurationForDomain(TypeMirror domainType) {

		if (!isSupportedType(domainType)) {
			return null;
		};

		Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(TransferObjectMapping.class);
		for (Element annotatedElement : elementsAnnotatedWith) {
			if (annotatedElement.asType().getKind().equals(TypeKind.DECLARED)) {
				ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement((TypeElement)annotatedElement, processingEnv, roundEnv);
	
				if (configurationTypeElement.appliesForDomainType(domainType)) {
					return new ConfigurationTypeElement((DeclaredType)domainType, null, (TypeElement)annotatedElement, processingEnv, roundEnv);
				}
			}
		}

		if (getCommonConfigurations() != null) {
			for (Class<?> clazz: getCommonConfigurations()) {
				TypeElement configurationElement = processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
				if (configurationElement.getAnnotation(TransferObjectMapping.class) != null) {

					ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(configurationElement, processingEnv, roundEnv);

					if (configurationTypeElement.appliesForDomainType(domainType)) {
						return new ConfigurationTypeElement((DeclaredType)domainType, null, configurationElement, processingEnv, roundEnv);
					}
				}
			}
		}
		return null;
	}

	public ConfigurationTypeElement getConfigurationForDto(TypeMirror dtoType) {

		if (!isSupportedType(dtoType)) {
			return null;
		};

		//Configuration should be directly in the DTO - when its generated
		if (dtoType.getKind().equals(TypeKind.DECLARED)) {
			TransferObjectConfiguration transferObjectConfiguration = new TransferObjectConfiguration(((DeclaredType)dtoType).asElement(), processingEnv);
			if (transferObjectConfiguration.getMappingForDto((DeclaredType)dtoType) != null) {
				return new ConfigurationTypeElement(((DeclaredType)dtoType).asElement(), processingEnv, roundEnv);
			}
		}
		
		Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(TransferObjectMapping.class);
		for (Element annotatedElement : elementsAnnotatedWith) {
			if (annotatedElement.asType().getKind().equals(TypeKind.DECLARED)) {
				ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement((TypeElement)annotatedElement, processingEnv, roundEnv);
	
				if (configurationTypeElement.appliesForDtoType(dtoType)) {
					return new ConfigurationTypeElement(null, (DeclaredType)dtoType, (TypeElement)annotatedElement, processingEnv, roundEnv);
				}
			}
		}

		if (getCommonConfigurations() != null) {
			for (Class<?> clazz: getCommonConfigurations()) {
				TypeElement configurationElement = processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
				if (configurationElement.getAnnotation(TransferObjectMapping.class) != null) {

					ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(configurationElement, processingEnv, roundEnv);

					if (configurationTypeElement.appliesForDtoType(dtoType)) {
						return new ConfigurationTypeElement(null, (DeclaredType)dtoType, configurationElement, processingEnv, roundEnv);
					}
				}
			}
		}
		return null;
	}
}