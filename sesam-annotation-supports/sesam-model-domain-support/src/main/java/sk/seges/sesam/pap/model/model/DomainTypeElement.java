package sk.seges.sesam.pap.model.model;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.builder.api.TypeMirrorConverter;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

public class DomainTypeElement extends TomBaseElement {

	private TypeMirror domainType;

	private final TypeMirror dtoType;
	
	private boolean configurationTypeInitialized = false;
	private ConfigurationTypeElement configurationTypeElement;
	
	private DomainTypeElement superClassDomainType;
	
	private boolean isInterface = false;
	
	DomainTypeElement(TypeMirror domainType, TypeMirror dtoType, ConfigurationTypeElement configurationTypeElement, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		
		this.domainType = domainType;
		this.dtoType = dtoType;
		this.configurationTypeElement = configurationTypeElement;
		this.configurationTypeInitialized = true;
		
		initialize();
	}

	@Override
	protected ImmutableType getDelegateImmutableType() {
		if (domainType != null) {
			return new NameTypesUtils(processingEnv).toImmutableType(domainType);
		}
		return new NameTypesUtils(processingEnv).toImmutableType(dtoType);
	}

	private void initialize() {
		if (dtoType != null && dtoType.getKind().equals(TypeKind.DECLARED) && ((DeclaredType)dtoType).getTypeArguments().size() > 0) {
			NamedType type = getNameTypesUtils().toType(dtoType);
			HasTypeParameters result = TypedClassBuilder.get(getDelegateImmutableType(), ((HasTypeParameters)type).getTypeParameters());
			setDelegateImmutableType(result);
			this.domainType = getNameTypesUtils().fromType(result);
		}
	}

	class TypeMirrorDomainHandler implements TypeMirrorConverter {

		@Override
		public NamedType handleType(TypeMirror type) {
			return new DtoTypeElement(type, processingEnv, roundEnv).getDomainTypeElement();
		}	
	}
	
	class NameTypesDomainUtils extends NameTypesUtils {

		public NameTypesDomainUtils(ProcessingEnvironment processingEnv) {
			super(processingEnv);
		}
		
		@Override
		protected TypeMirrorConverter getTypeMirrorConverter() {
			return new TypeMirrorDomainHandler();
		}
	}

	@Override
	protected NameTypesDomainUtils getNameTypesUtils() {
		return new NameTypesDomainUtils(processingEnv);
	}
	
	/**
	 * When there is only one configuration for domain type, it is OK ... but for multiple DTOs created from one domain type it can
	 * leads to unexpected results
	 */
	@Deprecated
	public DomainTypeElement(TypeMirror domainType, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		
		this.domainType = domainType;
		this.dtoType = null;
	}
	
	void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}
	
	public boolean isInterface() {
		return isInterface;
	}

	public TypeElement asElement() {
		if (asType().getKind().equals(TypeKind.DECLARED)) {
			return (TypeElement)((DeclaredType)asType()).asElement();
		}
		return null;
	}

	public TypeMirror asType() {
		return domainType;
	}

	public ConfigurationTypeElement getConfigurationTypeElement() {
		if (!configurationTypeInitialized) {
			this.configurationTypeElement = getConfigurationForDomain(domainType);
			this.configurationTypeInitialized = true;
		}
		return configurationTypeElement;
	}

	public DtoTypeElement getDtoTypeElement() {
		if (getConfigurationTypeElement() == null) {
			return null;
		}
		
		return getConfigurationTypeElement().getDtoTypeElement();
	}
	
	public DomainTypeElement getSuperClass() {
		
		if (!asType().getKind().equals(TypeKind.DECLARED)) {
			return null;
		}
		
		Element element = ((DeclaredType)asType()).asElement();
		
		if (element.getKind().equals(ElementKind.CLASS) || element.getKind().equals(ElementKind.INTERFACE)) {
			TypeElement typeElement = (TypeElement)element;
			if (typeElement.getSuperclass() != null && typeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
				if (superClassDomainType == null) {
					TypeMirror domainSuperClass = typeElement.getSuperclass();
					
					ConfigurationTypeElement configurationElement = getConfigurationForDomain(domainSuperClass);
					
					if (configurationElement != null) {
						superClassDomainType = configurationElement.getDomainTypeElement();
//						superClassDomainType = new DomainTypeElement(domainSuperClass, processingEnv, roundEnv);
					}
				}
				return superClassDomainType;
			}
		}
		
		return null;
	}
	
	protected ConfigurationTypeElement getConfigurationForDomain(TypeMirror domainType) {

		if (domainType.getKind().isPrimitive() || domainType.getKind().equals(TypeKind.NONE)
				|| domainType.getKind().equals(TypeKind.NULL) || domainType.getKind().equals(TypeKind.ERROR)) {
			// cannot cast to DTO
			return null;
		}

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

}