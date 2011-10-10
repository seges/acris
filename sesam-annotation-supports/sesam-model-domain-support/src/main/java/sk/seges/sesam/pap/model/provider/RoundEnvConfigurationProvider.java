package sk.seges.sesam.pap.model.provider;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;

import sk.seges.sesam.core.model.converter.CollectionConfiguration;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectConfiguration;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class RoundEnvConfigurationProvider implements ConfigurationProvider {

	protected final TransferObjectProcessingEnvironment processingEnv;
	protected final RoundEnvironment roundEnv;
	
	public RoundEnvConfigurationProvider(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
	}
	
	protected Class<?>[] getCommonConfigurations() {
		return new Class<?> [] {
				CollectionConfiguration.class
		};
	}

	protected boolean isSupportedType(MutableTypeMirror type) {
		return (!type.getKind().equals(MutableTypeKind.PRIMITIVE));
	}
	
	public ConfigurationTypeElement getConfigurationForDomain(MutableTypeMirror domainType) {

		if (!isSupportedType(domainType)) {
			return null;
		};

		Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(TransferObjectMapping.class);
		for (Element annotatedElement : elementsAnnotatedWith) {
			if (annotatedElement.asType().getKind().equals(TypeKind.DECLARED)) {
				ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement((TypeElement)annotatedElement, processingEnv, roundEnv);
	
				if (configurationTypeElement.appliesForDomainType(domainType)) {
					return new ConfigurationTypeElement((MutableDeclaredType)domainType, null, (TypeElement)annotatedElement, processingEnv, roundEnv);
				}
			}
		}

		if (getCommonConfigurations() != null) {
			for (Class<?> clazz: getCommonConfigurations()) {
				TypeElement configurationElement = processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
				if (configurationElement.getAnnotation(TransferObjectMapping.class) != null) {

					ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(configurationElement, processingEnv, roundEnv);

					if (configurationTypeElement.appliesForDomainType(domainType)) {
						return new ConfigurationTypeElement((MutableDeclaredType)domainType, null, configurationElement, processingEnv, roundEnv);
					}
				}
			}
		}
		return null;
	}

	public ConfigurationTypeElement getConfigurationForDto(MutableTypeMirror dtoType) {

		if (!isSupportedType(dtoType)) {
			return null;
		};

		//Configuration should be directly in the DTO - when its generated
		if (dtoType.getKind().isDeclared() && (((MutableDeclaredType)dtoType).asType()) != null && ((MutableDeclaredType)dtoType).asType().getKind().equals(TypeKind.DECLARED)) {
			TransferObjectConfiguration transferObjectConfiguration = new TransferObjectConfiguration(((DeclaredType)((MutableDeclaredType)dtoType).asType()).asElement(), processingEnv);
			if (transferObjectConfiguration.getMappingForDto((MutableDeclaredType)dtoType) != null) {
				return new ConfigurationTypeElement(((DeclaredType)((MutableDeclaredType)dtoType).asType()).asElement(), processingEnv, roundEnv);
			}
		}
		
		Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(TransferObjectMapping.class);
		for (Element annotatedElement : elementsAnnotatedWith) {
			if (annotatedElement.asType().getKind().equals(TypeKind.DECLARED)) {
				ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement((TypeElement)annotatedElement, processingEnv, roundEnv);
	
				if (configurationTypeElement.appliesForDtoType(dtoType)) {
					return new ConfigurationTypeElement(null, (MutableDeclaredType)dtoType, (TypeElement)annotatedElement, processingEnv, roundEnv);
				}
			}
		}

		if (getCommonConfigurations() != null) {
			for (Class<?> clazz: getCommonConfigurations()) {
				TypeElement configurationElement = processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
				if (configurationElement.getAnnotation(TransferObjectMapping.class) != null) {

					ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(configurationElement, processingEnv, roundEnv);

					if (configurationTypeElement.appliesForDtoType(dtoType)) {
						return new ConfigurationTypeElement(null, (MutableDeclaredType)dtoType, configurationElement, processingEnv, roundEnv);
					}
				}
			}
		}
		return null;
	}
}