package sk.seges.sesam.pap.model.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.domain.DomainTypeVariable;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

class DomainDeclared extends TomBaseDeclaredType implements DomainDeclaredType {

	private boolean configurationTypeInitialized = false;
	protected ConfigurationTypeElement configurationTypeElement;
	protected final ConfigurationProvider[] configurationProviders;

	protected MutableDeclaredType domainType;
	protected MutableDeclaredType dtoType;

	private DomainDeclaredType superClassDomainType;

	private boolean idMethodInitialized = false;
	private ExecutableElement idMethod;

	protected DomainDeclared(MutableDeclaredType domainType, MutableDeclaredType dtoType, ConfigurationTypeElement configurationTypeElement, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		
		this.domainType = domainType;
		this.dtoType = dtoType;
		this.configurationTypeElement = configurationTypeElement;
		this.configurationProviders = getConfigurationProviders(configurationProviders);

		initialize();
	}

	/**
	 * When there is only one configuration for domain type, it is OK ... but for multiple DTOs created from one domain type it can
	 * leads to unexpected results
	 */
	@Deprecated
	public DomainDeclared(DeclaredType domainType, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		
		this.domainType = processingEnv.getTypeUtils().toMutableType(domainType);
		this.dtoType = null;

		this.configurationProviders = getConfigurationProviders(configurationProviders);
	}

	/**
	 * When there is only one configuration for domain type, it is OK ... but for multiple DTOs created from one domain type it can
	 * leads to unexpected results
	 */
	@Deprecated
	public DomainDeclared(PrimitiveType domainType, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		
		this.domainType = (MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(domainType);
		this.dtoType = null;

		this.configurationProviders = getConfigurationProviders(configurationProviders);
	}

	/**
	 * When there is only one configuration for domain type, it is OK ... but for multiple DTOs created from one domain type it can
	 * leads to unexpected results
	 */
	@Deprecated
	public DomainDeclared(MutableDeclaredType domainType, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		
		this.domainType = domainType;
		this.dtoType = null;
		
		this.configurationProviders = getConfigurationProviders(configurationProviders);
	}

	@Override
	public ConfigurationTypeElement getConfiguration() {
		if (!configurationTypeInitialized) {
			this.configurationTypeElement = getConfiguration(domainType);
			this.configurationTypeInitialized = true;
		}
		return configurationTypeElement;
	}

	private ConfigurationTypeElement getConfiguration(MutableTypeMirror domainType) {
		for (ConfigurationProvider configurationProvider: configurationProviders) {
			ConfigurationTypeElement configurationForDomain = configurationProvider.getConfigurationForDomain(domainType);
			if (configurationForDomain != null) {
				configurationForDomain.setConfigurationProviders(configurationProviders);
				return configurationForDomain;
			}
		}
		
		return null;
	}

	@Override
	public ConverterTypeElement getConverter() {
		if (getConfiguration() == null) {
			return null;
		}
		
		return getConfiguration().getConverter();
	}


	@Override
	public DtoDeclaredType getDto() {
		if (getConfiguration() == null) {
			if (domainType != null) {
				return new DtoDeclared(domainType, processingEnv, roundEnv, configurationProviders);
			}
			
			return null;
		}
		
		return getConfiguration().getDto();
	}

	public ExecutableElement getIdMethod(EntityResolver entityResolver) {
		
		if (!idMethodInitialized) {
			List<ExecutableElement> overridenMethods =  new ArrayList<ExecutableElement>();
			
			if (getConfiguration() != null) {
				overridenMethods = ElementFilter.methodsIn(getConfiguration().asElement().getEnclosedElements());
			}
			
			MappingType mappingType = MappingType.AUTOMATIC;
			
			if (getConfiguration() != null) {
				mappingType = getConfiguration().getMappingType();
			}
			
			for (ExecutableElement overridenMethod: overridenMethods) {
	
				String fieldName = TransferObjectHelper.getFieldPath(overridenMethod);
				
				ExecutableElement domainMethod = getGetterMethod(fieldName);
	
				if (domainMethod != null && entityResolver.isIdMethod(domainMethod)) {
					if (this.idMethod != null) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Multiple identifier methods were specified." + 
								this.idMethod.getSimpleName().toString() + " in the " + this.idMethod.getEnclosingElement().toString() + " class and " +
								domainMethod.getSimpleName().toString() + " in the configuration " + domainMethod.getEnclosingElement().toString(), 
								configurationTypeElement.asElement());
					}
					this.idMethod = domainMethod;
				}
			}
	
			TypeElement processingElement = asElement();
	
			while (processingElement != null) {
	
				List<ExecutableElement> methods = ElementFilter.methodsIn(processingElement.getEnclosedElements());
		
				if (mappingType.equals(MappingType.AUTOMATIC)) {
					for (ExecutableElement method: methods) {
						if (MethodHelper.isGetterMethod(method) && toHelper.hasSetterMethod(asElement(), method) && method.getModifiers().contains(Modifier.PUBLIC) && entityResolver.isIdMethod(method)) {
							if (this.idMethod != null) {
								processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Multiple identifier methods were specified." + 
										this.idMethod.getSimpleName().toString() + " in the " + this.idMethod.getEnclosingElement().toString() + " class and " +
										method.getSimpleName().toString() + " in the configuration " + method.getEnclosingElement().toString(), 
										configurationTypeElement.asElement());
							}
							this.idMethod = method;
						}
					}
	
					for (TypeMirror interfaceType : processingElement.getInterfaces()) {
						if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
							ExecutableElement idMethod = ((DomainDeclared)getDomainForType(interfaceType)).getIdMethod(entityResolver);
							if (idMethod != null) {
								if (this.idMethod != null) {
									processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Multiple identifier methods were specified." + 
											this.idMethod.getSimpleName().toString() + " in the " + this.idMethod.getEnclosingElement().toString() + " class and " +
											idMethod.getSimpleName().toString() + " in the configuration " + idMethod.getEnclosingElement().toString(), 
											configurationTypeElement.asElement());
								}
								this.idMethod = idMethod;
							}
						}
					}
				}
								
				if (processingElement.getSuperclass() != null && processingElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
					processingElement = (TypeElement)((DeclaredType)processingElement.getSuperclass()).asElement();
				} else {
					processingElement = null;
				}
			}
	
			for (ExecutableElement overridenMethod: overridenMethods) {
				if (entityResolver.isIdMethod(overridenMethod)) {
					if (this.idMethod != null) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Multiple identifier methods were specified." + 
								this.idMethod.getSimpleName().toString() + " in the " + this.idMethod.getEnclosingElement().toString() + " class and " +
								overridenMethod.getSimpleName().toString() + " in the configuration " + overridenMethod.getEnclosingElement().toString(), 
								configurationTypeElement.asElement());
					}
					idMethod = overridenMethod;
				}
			}
			
			idMethodInitialized = true;
		}
		
		return idMethod;
	}
	
	public DomainType getDomainReference(String fieldName) {
		ExecutableElement getterMethod = getGetterMethod(fieldName);
		if (getterMethod == null) {
			return null;
		}
		if (getterMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
			return null;
		}
		return getDomainForType(getterMethod.getReturnType());
	}
	
	public ExecutableElement getGetterMethod(String fieldName) {
		return getMethod(new PathResolver(fieldName), MethodHelper.GETTER_PREFIX);
	}

	public ExecutableElement getSetterMethod(String fieldName) {
		return getMethod(new PathResolver(fieldName), MethodHelper.SETTER_PREFIX);
	}

	private ExecutableElement getMethod(String fieldName, String prefix) {
		return getMethod(new PathResolver(fieldName), prefix);
	}

	private ExecutableElement getMethod(PathResolver pathResolver, String prefix) {

		if (!pathResolver.hasNext()) {
			return null;
		}

		List<ExecutableElement> methods = ElementFilter.methodsIn(asElement().getEnclosedElements());

		String fieldName = pathResolver.next();

		for (ExecutableElement elementMethod : methods) {

			String currentPrefix = MethodHelper.GETTER_PREFIX;

			if (!pathResolver.hasNext()) {
				currentPrefix = prefix;
			}

			if (elementMethod.getModifiers().contains(Modifier.PUBLIC)
					&& elementMethod.getSimpleName().toString().equals(currentPrefix + MethodHelper.toMethod(fieldName))) {
				if (!pathResolver.hasNext()) {
					return elementMethod;
				}

				if (elementMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
					return ((DomainDeclared)getDomainForType(elementMethod.getReturnType())).getMethod(pathResolver.next(), prefix);
				}

				// incompatible types - nested path is expected, but declared
				// type was not found
				processingEnv.getMessager().printMessage(Kind.WARNING,
						"incompatible types - nested path (" + fieldName + ") is expected, but declared type was not found ", asElement());
				return null;
			}
		}

		if (asElement().getKind().equals(ElementKind.CLASS) || asElement().getKind().equals(ElementKind.INTERFACE)) {

			TypeElement typeElement = (TypeElement) asElement();
			if (typeElement.getSuperclass() != null && typeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
				pathResolver.reset();
				DomainType domainType = getDomainForType(typeElement.getSuperclass());
				return ((DomainDeclared)domainType).getMethod(pathResolver, prefix);
			}
		}

		return null;
	}

	public DomainType getId(EntityResolver entityResolver) {
		ExecutableElement idMethod = getIdMethod(entityResolver);
		
		if (idMethod == null) {
			return null;
		}
		
		return getDomainForType(idMethod.getReturnType());
	}

	@Override
	public DomainDeclaredType getSuperClass() {
		if (!asType().getKind().equals(TypeKind.DECLARED)) {
			return null;
		}
		
		Element element = ((DeclaredType)asType()).asElement();
		
		if (element.getKind().equals(ElementKind.CLASS) || element.getKind().equals(ElementKind.INTERFACE)) {
			TypeElement typeElement = (TypeElement)element;
			if (typeElement.getSuperclass() != null && typeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
				if (superClassDomainType == null) {
					TypeMirror domainSuperClass = typeElement.getSuperclass();
					
					ConfigurationTypeElement configurationElement = getConfiguration(processingEnv.getTypeUtils().toMutableType(domainSuperClass));
					
					if (configurationElement != null) {
						superClassDomainType = configurationElement.getDomain();
					}
				}
				return superClassDomainType;
			}
		}
		
		return null;
	}

	
	@Override
	protected MutableDeclaredType getDelegate() {
		if (domainType != null) {
			return domainType;
		}
		return dtoType;
	}

	private DomainType getDomainForType(TypeMirror type) {
		return processingEnv.getTransferObjectUtils().getDomainType(type);
	}

	private void initialize() {
		if (dtoType != null && dtoType.getTypeVariables().size() > 0) {

			MutableTypeVariable[] typeVariables = new MutableTypeVariable[dtoType.getTypeVariables().size()];
			
			int i = 0;
			for (MutableTypeVariable typeVariable: dtoType.getTypeVariables()) {
				typeVariables[i++] = (DomainTypeVariable)processingEnv.getTransferObjectUtils().getDtoType(typeVariable).getDomain();
			}
			
			MutableDeclaredType result = setTypeVariables(typeVariables);
			stripTypeParametersTypes();
			this.domainType = result;
		}
	}

	public TypeElement asElement() {
		//TODO it is
		if (asType().getKind().equals(TypeKind.DECLARED)) {
			return (TypeElement)((DeclaredType)asType()).asElement();
		}
		return null;
	}

	public TypeMirror asType() {
		return getMutableTypesUtils().fromMutableType(domainType);
	}
}