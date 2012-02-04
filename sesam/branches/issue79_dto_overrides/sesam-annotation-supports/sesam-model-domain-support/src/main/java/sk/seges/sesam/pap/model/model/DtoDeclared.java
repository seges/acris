package sk.seges.sesam.pap.model.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

class DtoDeclared extends TomDeclaredConfigurationHolder implements GeneratedClass, DtoDeclaredType {

	private final boolean generated;
	private final MutableDeclaredType dtoType;
	
	private boolean isInterface = false;
	
	DtoDeclared(MutableDeclaredType dtoType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(envContext, configurationContext);

		this.dtoType = dtoType;
		this.generated = false;
				
		initialize();
	}

	DtoDeclared(DeclaredType dtoType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(envContext, configurationContext);

		this.dtoType = getTypeUtils().toMutableType(dtoType);
		this.generated = false;
				
		initialize();
	}

	DtoDeclared(EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(envContext, configurationContext);

		this.generated = true;
		this.dtoType = null;

		initialize();
		
		setKind(MutableTypeKind.CLASS);
		
		DomainDeclaredType superClassDomainType = getDomain().getSuperClass();

		if (superClassDomainType != null) {
			DtoDeclaredType superclassDto = superClassDomainType.getDto();
			MutableDeclaredType mutableSuperclassType = getTypeUtils().toMutableType((DeclaredType)superClassDomainType.asConfigurationElement().asType());
			
			if (superclassDto instanceof MutableDeclaredType) {
				//TODO convert type variables also
				((MutableDeclaredType)superclassDto).setTypeVariables(mutableSuperclassType.getTypeVariables().toArray(new MutableTypeVariable[] {}));
				setSuperClass((MutableDeclaredType)superclassDto);
			} else {
				//TODO log something here
			}
		}

		ConfigurationTypeElement domainDefinitionConfiguration = getDomainDefinitionConfiguration();
		
		if (domainDefinitionConfiguration != null && domainDefinitionConfiguration.asConfigurationElement().asType().getKind().equals(TypeKind.DECLARED)) {
			List<? extends TypeMirror> interfaces = ((TypeElement)domainDefinitionConfiguration.asConfigurationElement()).getInterfaces();
			
			List<MutableTypeMirror> interfaceTypes = new ArrayList<MutableTypeMirror>();
			
			if (interfaces != null) {
				for (TypeMirror interfaceType: interfaces) {
					MutableDeclaredType mutableType = (MutableDeclaredType) getTypeUtils().toMutableType(interfaceType);
					mutableType.prefixTypeParameter(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX);
					interfaceTypes.add(mutableType);
				}
			}
			
			MutableTypeMirror superClass = super.getSuperClass();
			if (superClass == null) {
				if (domainDefinitionConfiguration.ensureDelegateType().getKind().isDeclared() && ((MutableDeclaredType) domainDefinitionConfiguration.ensureDelegateType()).getSuperClass() != null) {
					setSuperClass(((MutableDeclaredType) domainDefinitionConfiguration.ensureDelegateType()).getSuperClass());
				} else {
					//TODO check if it is not already there
					interfaceTypes.add(getTypeUtils().toMutableType(Serializable.class));
				}
			}
			
			setInterfaces(interfaceTypes);
		}
	}
		
	DtoDeclared(PrimitiveType dtoType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(envContext, configurationContext);

		this.dtoType = (MutableDeclaredType) getTypeUtils().toMutableType(dtoType);
		this.generated = false;
				
		initialize();
	}

	protected List<ConfigurationTypeElement> getConfigurationsForType() {
		return getConfigurations(this.dtoType);
	};

	private List<ConfigurationTypeElement> getConfigurations(MutableTypeMirror dtoType) {
		for (ConfigurationProvider configurationProvider: getConfigurationProviders()) {
			List<ConfigurationTypeElement> configurationsForDto = configurationProvider.getConfigurationsForDto(dtoType);
			if (configurationsForDto != null && configurationsForDto.size() > 0) {
				return configurationsForDto;
			}
		}
		
		return new ArrayList<ConfigurationTypeElement>();
	}
	
	protected MutableDeclaredType getDelegate() {
		if (dtoType != null) {
			return (MutableDeclaredType) getTypeUtils().toMutableType(dtoType);
		}
		return getGeneratedDtoTypeFromConfiguration();
	};

	private void initialize() {
		if (getConfigurations().size() == 0) {
			return;
		}

		ConfigurationTypeElement domainDefinitionConfiguration = getDomainDefinitionConfiguration();

		if (domainDefinitionConfiguration.getDomain().hasTypeParameters()) {
		
			List<? extends TypeMirror> typeArguments = ((DeclaredType)domainDefinitionConfiguration.getDomain().asType()).getTypeArguments();
			List<MutableTypeVariable> dtoTypeVariables = new LinkedList<MutableTypeVariable>();
			if (typeArguments.size() == getTypeVariables().size()) {
				for (TypeMirror domainTypeVariable: typeArguments) {
					DtoType dtoTypeVariable = getTransferObjectUtils().getDomainType(domainTypeVariable).getDto();
					if (dtoTypeVariable instanceof MutableTypeVariable) {
						dtoTypeVariables.add((MutableTypeVariable)dtoTypeVariable);
					} else {
						dtoTypeVariables.add(getTypeUtils().getTypeVariable(null, dtoTypeVariable));
					}
				}
			}
			setDelegate(getDelegate().clone().setTypeVariables(dtoTypeVariables.toArray(new MutableTypeVariable[] {})).stripTypeParametersTypes());
		}
	}
	
	protected PackageValidatorProvider getPackageValidationProvider() {
		return new DefaultPackageValidatorProvider();
	}

	private MutableDeclaredType getGeneratedDtoTypeFromConfiguration() {

		ConfigurationTypeElement domainDefinitionConfiguration = getDomainDefinitionConfiguration();
		
		MutableDeclaredType outputType = ((MutableDeclaredType) domainDefinitionConfiguration.ensureDelegateType()).clone();
		
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
		
		ConfigurationTypeElement domainDefinitionConfiguration = getDomainDefinitionConfiguration();
		
		if (domainDefinitionConfiguration != null) {
			result = domainDefinitionConfiguration.getDomain();
		}
		
		if (result != null) {
			return result;
		}

		if (dtoType != null) {
			return (DomainDeclaredType) getTransferObjectUtils().getDomainType(dtoType);
		}

		return null;
	}

	public DtoDeclared getSuperClass() {

		MutableDeclaredType superClassDelegate = ensureDelegateType().getSuperClass();
		if (superClassDelegate != null) {
			return (DtoDeclared) getTransferObjectUtils().getDtoType(superClassDelegate);
		}

		return null;
	}
	
	public boolean isInterface() {
		return isInterface;
	}
	
	void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}
	
	public ConverterTypeElement getConverter() {
		ConfigurationTypeElement converterDefinitionConfiguration = getConverterDefinitionConfiguration();
		
		if (converterDefinitionConfiguration == null) {
			return null;
		}
		
		return converterDefinitionConfiguration.getConverter();
	}

	public ExecutableElement getIdMethod(EntityResolver entityResolver) {

		ConfigurationTypeElement domainDefinitionConfiguration = getDomainDefinitionConfiguration();
		
		if (domainDefinitionConfiguration != null) {
			List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(domainDefinitionConfiguration.asConfigurationElement().getEnclosedElements());
	
			DomainDeclaredType domainType = domainDefinitionConfiguration.getDomain();
	
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
		
		if (domainDefinitionConfiguration != null) {
			if (idMethod != null && !domainDefinitionConfiguration.isFieldIgnored(MethodHelper.toField(idMethod))) {
				return idMethod;
			}
			
			return null;
		}
		return idMethod;
	}
}