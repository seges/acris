package sk.seges.sesam.pap.model.model;

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
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class DomainTypeElement extends TomBaseElement {

	private TypeMirror domainType;

	private final TypeMirror dtoType;
	private final ConfigurationProvider[] configurationProviders;
	
	private boolean configurationTypeInitialized = false;
	private ConfigurationTypeElement configurationTypeElement;
	
	private DomainTypeElement superClassDomainType;
	
	private boolean isInterface = false;
	
	DomainTypeElement(TypeMirror domainType, TypeMirror dtoType, ConfigurationTypeElement configurationTypeElement, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		
		this.domainType = domainType;
		this.dtoType = dtoType;
		this.configurationTypeElement = configurationTypeElement;
		this.configurationTypeInitialized = true;
		
		this.configurationProviders = getConfigurationProviders(configurationProviders);
		
		initialize();
	}

	/**
	 * When there is only one configuration for domain type, it is OK ... but for multiple DTOs created from one domain type it can
	 * leads to unexpected results
	 */
	@Deprecated
	public DomainTypeElement(TypeMirror domainType, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		
		this.domainType = domainType;
		this.dtoType = null;
		
		this.configurationProviders = getConfigurationProviders(configurationProviders);
	}

	class TypeMirrorDomainHandler implements TypeMirrorConverter {

		@Override
		public NamedType handleType(TypeMirror type) {
			DomainTypeElement domain = new DtoTypeElement(type, processingEnv, roundEnv, configurationProviders).getDomainTypeElement();
			if (domain != null) {
				return domain;
			}
			
			return new NameTypesUtils(processingEnv).toType(type);
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

	@Override
	protected NameTypesDomainUtils getNameTypesUtils() {
		return new NameTypesDomainUtils(processingEnv);
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
			this.configurationTypeElement = getConfiguration(domainType);
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
	
	private ConfigurationTypeElement getConfiguration(TypeMirror domainType) {
		for (ConfigurationProvider configurationProvider: configurationProviders) {
			ConfigurationTypeElement configurationForDomain = configurationProvider.getConfigurationForDomain(domainType);
			if (configurationForDomain != null) {
				configurationForDomain.setConfigurationProviders(configurationProviders);
				return configurationForDomain;
			}
		}
		
		return null;
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
					
					ConfigurationTypeElement configurationElement = getConfiguration(domainSuperClass);
					
					if (configurationElement != null) {
						superClassDomainType = configurationElement.getDomainTypeElement();
					}
				}
				return superClassDomainType;
			}
		}
		
		return null;
	}
}