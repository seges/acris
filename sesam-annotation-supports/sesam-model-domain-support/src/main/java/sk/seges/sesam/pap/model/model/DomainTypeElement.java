package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class DomainTypeElement extends TomBaseElement {

	private final TypeMirror domainType;
	private final ConfigurationTypeElement configurationTypeElement;
	
	private DomainTypeElement superClassDomainType;
	
	private boolean isInterface = false;
	
	DomainTypeElement(ConfigurationTypeElement configurationTypeElement, TypeMirror domainType, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		
		this.domainType = domainType;
		this.configurationTypeElement = configurationTypeElement;
		
		setDelegateImmutableType(getNameTypesUtils().toImmutableType(domainType));
	}

	public DomainTypeElement(TypeMirror domainType, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		
		this.domainType = domainType;
		
		setDelegateImmutableType(getNameTypesUtils().toImmutableType(domainType));
		this.configurationTypeElement = toHelper.getConfigurationForDomain(domainType);
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
		return configurationTypeElement;
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
					
					ConfigurationTypeElement configurationElement = toHelper.getConfigurationForDomain(domainSuperClass);
					
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
}