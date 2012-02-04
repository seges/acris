package sk.seges.sesam.pap.model.model;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;

public class ConfigurationTypeElement extends TomBaseType {

	private DtoDeclared dtoDeclaredType;
	private boolean dtoTypeElementInitialized = false;

	private DomainDeclared domainDeclaredType;
	private boolean domainTypeElementInitialized = false;
	
	private ConfigurationTypeElement delegateConfigurationTypeElement;
	private boolean delegateConfigurationTypeElementInitialized = false;

	private ConverterTypeElement converterTypeElement;
	private boolean converterTypeElementInitialized = false;

	private final Element configurationElement;
	protected final TransferObjectMappingAccessor transferObjectConfiguration;

	private final MutableDeclaredType domainType;
	protected final MutableDeclaredType dtoType;

	private final String canonicalName;

	private final ConfigurationContext configurationContext;
	
	public ConfigurationTypeElement(MutableDeclaredType domainType, MutableDeclaredType dtoType, TypeElement configurationElement, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		super(envContext);

		this.configurationContext = configurationContext;		
		this.configurationElement = configurationElement;
		this.domainType = domainType;
		this.dtoType = dtoType;
		
		this.canonicalName = configurationElement.getQualifiedName().toString();
		
		this.transferObjectConfiguration = new TransferObjectMappingAccessor(configurationElement, envContext.getProcessingEnv());
		if (this.transferObjectConfiguration.getReferenceMapping() == null) {
			this.transferObjectConfiguration.setReferenceMapping(transferObjectConfiguration.getMappingForDto(dtoType));
		}
	}
	
	public ConfigurationTypeElement(ExecutableElement configurationElementMethod, DomainDeclaredType returnType, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		
		super(envContext);

		this.configurationContext = configurationContext;
		this.configurationElement = configurationElementMethod;
		this.domainType = returnType;
		if (configurationElementMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
			this.dtoType = (MutableDeclaredType) getTypeUtils().toMutableType(configurationElementMethod.getReturnType()); 
		} else {
			this.dtoType = returnType;
		}
		this.canonicalName = configurationElement.getSimpleName().toString();

		this.transferObjectConfiguration = new TransferObjectMappingAccessor(configurationElement, envContext.getProcessingEnv());
	}

	public ConfigurationTypeElement(Element configurationElement, EnvironmentContext<TransferObjectProcessingEnvironment> envContext, ConfigurationContext configurationContext) {
		
		super(envContext);

		this.configurationContext = configurationContext;
		this.configurationElement = configurationElement;
		this.domainType = null;
		this.dtoType = null;

		this.canonicalName = configurationElement.getSimpleName().toString();

		this.transferObjectConfiguration = new TransferObjectMappingAccessor(configurationElement, envContext.getProcessingEnv());
	}

	@Override
	protected MutableTypeMirror getDelegate() {
		if (configurationElement.getKind().equals(ElementKind.METHOD)) {
			return getTypeUtils().toMutableType((ExecutableElement) configurationElement);
		}
		return getTypeUtils().toMutableType((DeclaredType)configurationElement.asType());
	}

	public boolean isValid() {
		if (transferObjectConfiguration == null) {
			return false;
		}
		
		return transferObjectConfiguration.isValid();
	}
	
	private ConverterTypeElement getConverter(TransferObjectMappingAccessor transferObjectConfiguration) {
		
		if (!transferObjectConfiguration.isValid()) {
			return null;
		}
		
		TypeElement converter = transferObjectConfiguration.getConverter();
		if (converter != null && transferObjectConfiguration.isConverterGenerated()) {
			return new ConverterTypeElement(this, converter, envContext);
		}
				
		Element configurationElement = asConfigurationElement();
		
		if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED) || !transferObjectConfiguration.isConverterGenerated()) {
			return null;
		}

		return new ConverterTypeElement(this, envContext);
	}

	public DomainDeclaredType getRawDomain() {
		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getRawDomain();
		}

		if (transferObjectConfiguration.isValid()) {
			TypeElement domainInterface = transferObjectConfiguration.getDomainInterface();
			if (domainInterface != null) {
				DomainDeclared result = new DomainDeclared((MutableDeclaredType) getTypeUtils().toMutableType(domainInterface.asType()), dtoType, envContext, configurationContext);
				this.domainDeclaredType.setKind(MutableTypeKind.INTERFACE);
				return result;
			}
		}
		
		return getDomain();
	}

	public DomainDeclaredType getDomain() {
		if (!domainTypeElementInitialized) {
			MutableDeclaredType domainType = null;
			
			if (this.domainType != null) {
				domainType = this.domainType;
			} else {
				TypeElement domainTypeElement = transferObjectConfiguration.getEvaluatedDomainType();
				if (domainTypeElement != null) {
					domainType = getTypeUtils().toMutableType((DeclaredType) domainTypeElement.asType());
				}
			}

			if (domainType != null) {
				this.domainDeclaredType = new DomainDeclared(domainType, dtoType, envContext, configurationContext);
				domainDeclaredType.prefixTypeParameter(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX);
			} else {
				if (this.domainType == null) {
					TypeElement domainInterface = transferObjectConfiguration.getDomainInterface();
					if (domainInterface != null) {
						if (dtoType != null && getTypeUtils().implementsType(dtoType, getTypeUtils().toMutableType(domainInterface.asType()))) {
							this.domainDeclaredType = new DomainDeclared(null, dtoType, envContext, configurationContext);
						} else {
							this.domainDeclaredType = new DomainDeclared((MutableDeclaredType) getTypeUtils().toMutableType(domainInterface.asType()), dtoType, envContext, configurationContext);
							this.domainDeclaredType.setKind(MutableTypeKind.INTERFACE);
						}
					}
				}
			}

			if (this.domainDeclaredType == null) {
				this.dtoTypeElementInitialized = true;
				this.dtoDeclaredType = null;
			}
			
			this.domainTypeElementInitialized = true;
		}
		return domainDeclaredType;
	}

	boolean hasGeneratedDto() {
		return transferObjectConfiguration.isDtoGenerated() != false && !hasDtoSpecified();
	}

	boolean hasGeneratedConverter() {
		return transferObjectConfiguration.isConverterGenerated() != false && !hasConverterSpecified();
	}
	
	boolean hasConverterSpecified() {
		return transferObjectConfiguration.getConverter() != null;
	}

	boolean hasDomainSpecified() {
		return transferObjectConfiguration.getDomain() != null || transferObjectConfiguration.getDomainInterface() != null;
	}
	
	boolean hasDtoSpecified() {
		return transferObjectConfiguration.getDto() != null || transferObjectConfiguration.getDtoInterface() != null;
	}
	
	public DtoDeclaredType getRawDto() {
		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getRawDto();
		}

		if (transferObjectConfiguration.isValid()) {
			TypeElement dtoInterface = transferObjectConfiguration.getDtoInterface();
			if (dtoInterface != null) {
				DtoDeclared result = new DtoDeclared(getTypeUtils().toMutableType((DeclaredType) dtoInterface.asType()), envContext, configurationContext);
				result.setInterface(true);
				return result;
			}
		}
		
		return getDto();
	}
	
	public DtoDeclaredType getDto() {
		
		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getDto();
		}

		if (!dtoTypeElementInitialized) {
			
			MutableDeclaredType dtoType = null;
			
			if (this.dtoType != null) {
				dtoType = this.dtoType;
			} else {
				if (transferObjectConfiguration.isValid()) {
					TypeElement dtoTypeElement = transferObjectConfiguration.getDto();
					if (dtoTypeElement != null) {
						dtoType = getTypeUtils().toMutableType((DeclaredType) dtoTypeElement.asType());
					}
				}
			}
			
			if (dtoType != null) {
				this.dtoDeclaredType = new DtoDeclared(dtoType, envContext, configurationContext);
			} else {
				if (this.dtoType == null && transferObjectConfiguration.isValid()) {
					TypeElement dtoInterface = transferObjectConfiguration.getDtoInterface();
					if (dtoInterface != null) {
						if (domainType != null && getTypeUtils().implementsType(domainType, getTypeUtils().toMutableType(dtoInterface.asType()))) {
							this.dtoDeclaredType = new DtoDeclared(domainType, envContext, configurationContext);
						} else {
							this.dtoDeclaredType = new DtoDeclared(getTypeUtils().toMutableType((DeclaredType) dtoInterface.asType()), envContext, configurationContext);
							this.dtoDeclaredType.setInterface(true);
						}
					}
				}

				if (this.dtoDeclaredType == null) {
					Element configurationElement = asConfigurationElement();
					
					if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
						return null;
					}

					this.dtoDeclaredType = new DtoDeclared(envContext, configurationContext);
				}
			}

			this.dtoTypeElementInitialized = true;
		}
		
		return dtoDeclaredType;
	}
	
	public ConverterTypeElement getConverter() {

		if (!converterTypeElementInitialized) {
			this.converterTypeElement = getConverter(transferObjectConfiguration);
			this.converterTypeElementInitialized = true;
		}
		
		if (converterTypeElement != null) {
			if (converterTypeElement.isGenerated()) {
				if (getDelegateConfigurationTypeElement() != null) {
					ConverterTypeElement delegateConverterTypeElement = getDelegateConfigurationTypeElement().getConverter();
					if (delegateConverterTypeElement != null) {
						return delegateConverterTypeElement;
					}
				}
			}
			return converterTypeElement;
		}

		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getConverter();
		}
		
		return null;
	}
	
	public Element asConfigurationElement() {
		return configurationElement;
	}
	
	public ConfigurationTypeElement getDelegateConfigurationTypeElement() {
		if (!delegateConfigurationTypeElementInitialized) {
			if (transferObjectConfiguration.getConfiguration() != null) {
				this.delegateConfigurationTypeElement = new ConfigurationTypeElement(transferObjectConfiguration.getConfiguration(), envContext, configurationContext);
			} else {
				this.delegateConfigurationTypeElement = null;
			}

			this.delegateConfigurationTypeElementInitialized = true;
		}
		return delegateConfigurationTypeElement;
	}
	
	public boolean appliesForDomainType(MutableTypeMirror domainType) {
		
		//Configuration should be applied only for declared types like classes or interfaces
		if (!domainType.getKind().equals(MutableTypeKind.CLASS) &&
			!domainType.getKind().equals(MutableTypeKind.INTERFACE)) {
			return false;
		}
		
		return transferObjectConfiguration.getMappingForDomain((MutableDeclaredType)domainType) != null;
	}

	public boolean appliesForDtoType(MutableTypeMirror dtoType) {

		//Configuration should be applied only for declared types like classes or interfaces
		if (!dtoType.getKind().equals(MutableTypeKind.CLASS) &&
			!dtoType.getKind().equals(MutableTypeKind.INTERFACE)) {
				return false;
		}
		
		TransferObjectMapping mappingForDto = transferObjectConfiguration.getMappingForDto((MutableDeclaredType)dtoType);
		
		if (mappingForDto != null) {
			return true;
		}

		if (this.dtoType == null) {
			return (getDto() != null && getDto().getCanonicalName().equals(dtoType.toString(ClassSerializer.CANONICAL, false)));
		}
		
		return false;
	}

	public String getCanonicalName() {
		return canonicalName;
	}
	
	public MappingType getMappingType() {
		MappingType mappingType = MappingType.AUTOMATIC;
		Mapping mapping =  configurationElement.getAnnotation(Mapping.class);

		if (mapping != null) {
			mappingType = mapping.value();
		}
		
		return mappingType;
	}

	boolean isFieldIgnored(String field) {

		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(asConfigurationElement().getEnclosedElements());

		for (ExecutableElement overridenMethod : overridenMethods) {

			if (MethodHelper.toField(overridenMethod).equals(field)) {
				Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
				if (ignoreAnnotation != null) {
					return true;
				}
			}
		}

		return false;
	}
}