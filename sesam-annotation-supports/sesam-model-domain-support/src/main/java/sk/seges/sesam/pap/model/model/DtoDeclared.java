package sk.seges.sesam.pap.model.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.model.api.GeneratedClass;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

class DtoDeclared extends TomBaseDeclaredType implements GeneratedClass, DtoDeclaredType {

	private final ConfigurationTypeElement configurationTypeElement;

	private final boolean generated;
	private final MutableDeclaredType dtoType;
	private ConfigurationProvider[] configurationProviders;
	
	private boolean isInterface = false;
	
	DtoDeclared(ConfigurationTypeElement configurationTypeElement, MutableDeclaredType dtoTypeElement, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider ...configurationProviders) {
		super(processingEnv, roundEnv);
		this.configurationTypeElement = configurationTypeElement;
		this.generated = false;
		this.dtoType = dtoTypeElement;
		this.configurationProviders = getConfigurationProviders(configurationProviders);
		
		initialize();
	}

	DtoDeclared(ConfigurationTypeElement configurationTypeElement, DeclaredType dtoType, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider ...configurationProviders) {
		super(processingEnv, roundEnv);
		
		this.configurationTypeElement = configurationTypeElement;
		this.generated = false;
		this.dtoType = processingEnv.getTypeUtils().toMutableType(dtoType);
		this.configurationProviders = getConfigurationProviders(configurationProviders);
		
		initialize();
	}

	DtoDeclared(ConfigurationTypeElement configurationTypeElement, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider ...configurationProviders) {
		super(processingEnv, roundEnv);

		this.configurationTypeElement = configurationTypeElement;
		this.generated = true;
		this.dtoType = null;
		this.configurationProviders = getConfigurationProviders(configurationProviders);

		initialize();
		
		setKind(MutableTypeKind.CLASS);
		
		DomainDeclaredType superClassDomainType = getDomain().getSuperClass();

		if (superClassDomainType != null) {
			DtoDeclaredType superclassDto = superClassDomainType.getDto();
			MutableDeclaredType mutableSuperclassType = processingEnv.getTypeUtils().toMutableType((DeclaredType)superClassDomainType.asElement().asType());
			
			if (superclassDto instanceof MutableDeclaredType) {
				//TODO convert type variables also
				((MutableDeclaredType)superclassDto).setTypeVariables(mutableSuperclassType.getTypeVariables().toArray(new MutableTypeVariable[] {}));
				setSuperClass((MutableDeclaredType)superclassDto);
			} else {
				//TODO log something here
			}
		}

		if (configurationTypeElement.asElement().asType().getKind().equals(TypeKind.DECLARED)) {
			List<? extends TypeMirror> interfaces = ((TypeElement)configurationTypeElement.asElement()).getInterfaces();
			
			Set<MutableTypeMirror> interfaceTypes = new HashSet<MutableTypeMirror>();
			
			if (interfaces != null) {
				for (TypeMirror interfaceType: interfaces) {
					interfaceTypes.add(processingEnv.getTypeUtils().toMutableType(interfaceType));
				}
			}
			
			MutableTypeMirror superClass = super.getSuperClass();
			if (superClass == null) {
				//TODO check if it is not already there
				interfaceTypes.add(processingEnv.getTypeUtils().toMutableType(Serializable.class));
			}
			
			setInterfaces(interfaceTypes);
		}
	}

	DtoDeclared(DeclaredType dtoType, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider ...configurationProviders) {
		super(processingEnv, roundEnv);

		this.dtoType = processingEnv.getTypeUtils().toMutableType(dtoType);
		this.configurationProviders = getConfigurationProviders(configurationProviders);
		this.configurationTypeElement = getConfiguration(this.dtoType);

		this.generated = false;
				
		initialize();
	}

	DtoDeclared(PrimitiveType dtoType, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider ...configurationProviders) {
		super(processingEnv, roundEnv);

		this.dtoType = (MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(dtoType);
		this.configurationProviders = getConfigurationProviders(configurationProviders);
		this.configurationTypeElement = getConfiguration(this.dtoType);

		this.generated = false;
				
		initialize();
	}

	DtoDeclared(MutableDeclaredType dtoType, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider ...configurationProviders) {
		super(processingEnv, roundEnv);

		this.dtoType = dtoType;
		this.configurationProviders = getConfigurationProviders(configurationProviders);
		this.configurationTypeElement = getConfiguration(this.dtoType);

		this.generated = false;
				
		initialize();
	}

	private ConfigurationTypeElement getConfiguration(MutableTypeMirror dtoType) {
		for (ConfigurationProvider configurationProvider: configurationProviders) {
			ConfigurationTypeElement configurationForDto = configurationProvider.getConfigurationForDto(dtoType);
			if (configurationForDto != null) {
				configurationForDto.setConfigurationProviders(configurationProviders);
				return configurationForDto;
			}
		}
		
		return null;
	}
	
	protected MutableDeclaredType getDelegate() {
		if (dtoType != null) {
			return (MutableDeclaredType) getMutableTypesUtils().toMutableType(dtoType);
		}
		return getGeneratedDtoTypeFromConfiguration(configurationTypeElement);
	};

	private void initialize() {
		if (configurationTypeElement == null) {
			return;
		}

		if (configurationTypeElement.getDomain().hasTypeParameters()) {
		
			List<? extends MutableTypeVariable> typeVariables = configurationTypeElement.getDomain().getTypeVariables();
			List<MutableTypeVariable> dtoTypeVariables = new LinkedList<MutableTypeVariable>();
			for (MutableTypeVariable domainTypeVariable: typeVariables) {
				DtoType dtoTypeVariable = processingEnv.getTransferObjectUtils().getDtoType(domainTypeVariable);
				dtoTypeVariables.add((MutableTypeVariable)dtoTypeVariable);
			}

			setDelegate(getDelegate().clone().setTypeVariables(dtoTypeVariables.toArray(new MutableTypeVariable[] {})));
		}
	}
	
	protected PackageValidatorProvider getPackageValidationProvider() {
		return new DefaultPackageValidatorProvider();
	}

	private MutableDeclaredType getGeneratedDtoTypeFromConfiguration(ConfigurationTypeElement configurationType) {

//		Element configurationElement = configurationType.asElement();
//		
//		if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
//			return null;
//		}

		//MutableDeclaredType configurationNameType = super.getMutableTypesUtils().toMutableType((DeclaredType) configurationTypeElement.asElement().asType());

		MutableDeclaredType outputType = configurationType.clone();
		
		PackageValidator packageValidator = getPackageValidationProvider().get(outputType)
				.moveTo(LocationType.SHARED).moveTo(LayerType.MODEL).clearType().moveTo(ImplementationType.DTO);
		outputType = outputType.changePackage(packageValidator);
		return outputType.getSimpleName().endsWith(TransferObjectHelper.DEFAULT_SUFFIX) ? outputType.setSimpleName(outputType
				.getSimpleName().substring(0, outputType.getSimpleName().length() - TransferObjectHelper.DEFAULT_SUFFIX.length()))
				: outputType.addClassSufix(TransferObjectHelper.DTO_SUFFIX);
	}

	@Override
	public boolean isGenerated() {
		return generated;
	}
	
	public DomainDeclaredType getDomain() {
		DomainDeclaredType result = null;
		if (configurationTypeElement != null) {
			result = configurationTypeElement.getDomain();
		}
		
		if (result != null) {
			return result;
		}

		if (dtoType != null) {
			return (DomainDeclaredType) processingEnv.getTransferObjectUtils().getDomainType(dtoType);
		}

		return null;
	}

	public DtoDeclared getSuperClass() {

		MutableDeclaredType superClassDelegate = ensureDelegateType().getSuperClass();
		if (superClassDelegate != null) {
			return (DtoDeclared) processingEnv.getTransferObjectUtils().getDtoType(superClassDelegate);
		}

		return null;
	}
	
	public boolean isInterface() {
		return isInterface;
	}
	
	void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}
	
	public ConfigurationTypeElement getConfiguration() {
		return configurationTypeElement;
	}
	
	public ConverterTypeElement getConverter() {
		if (configurationTypeElement == null) {
			return null;
		}
		
		return configurationTypeElement.getConverter();
	}

	public ExecutableElement getIdMethod(EntityResolver entityResolver) {

		if (getConfiguration() != null) {
			List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(getConfiguration().asElement().getEnclosedElements());
	
			DomainDeclaredType domainType = getConfiguration().getDomain();
	
			if (!domainType.asType().getKind().equals(TypeKind.DECLARED)) {
				return null;
			}
			
			for (ExecutableElement overridenMethod : overridenMethods) {
	
				Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
				if (ignoreAnnotation == null) {
	
					if (entityResolver.isIdMethod(overridenMethod)) {
						if (overridenMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
							return domainType.getGetterMethod(TransferObjectHelper.getFieldPath(overridenMethod));
						}
	
						return overridenMethod;
					}
				}
			}
		}
		
		ExecutableElement idMethod = getDomain().getIdMethod(entityResolver);
		
		if (getConfiguration() != null) {
			if (idMethod != null && !getConfiguration().isFieldIgnored(MethodHelper.toField(idMethod))) {
				return idMethod;
			}
			
			return null;
		}
		return idMethod;
	}
}