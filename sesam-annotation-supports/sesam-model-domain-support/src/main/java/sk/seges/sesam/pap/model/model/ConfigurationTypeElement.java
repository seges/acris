package sk.seges.sesam.pap.model.model;

import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class ConfigurationTypeElement extends TomBaseDeclaredType {

	private DtoDeclared dtoTypeElement;
	private boolean dtoTypeElementInitialized = false;

	private DomainTypeElement domainTypeElement;
	private boolean domainTypeElementInitialized = false;
	
	private ConfigurationTypeElement delegateConfigurationTypeElement;
	private boolean delegateConfigurationTypeElementInitialized = false;

	private ConverterTypeElement converterTypeElement;
	private boolean converterTypeElementInitialized = false;

	private final Element configurationElement;
	private final TransferObjectConfiguration transferObjectConfiguration;

	private final MutableDeclaredType domainType;
	private final DeclaredType dtoType;

	private final String canonicalName;
	private ConfigurationProvider[] configurationProviders;
	
	public ConfigurationTypeElement(MutableDeclaredType domainType, DeclaredType dtoType, TypeElement configurationElement, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		
		this.configurationElement = configurationElement;
		this.domainType = domainType;
		this.dtoType = dtoType;
		
		this.canonicalName = configurationElement.getQualifiedName().toString();
		
		this.transferObjectConfiguration = new TransferObjectConfiguration(configurationElement, processingEnv);
		if (this.transferObjectConfiguration.getReferenceMapping() == null) {
			this.transferObjectConfiguration.setReferenceMapping(transferObjectConfiguration.getMappingForDto(processingEnv.getTypeUtils().toMutableType(dtoType)));
		}
		this.configurationProviders = getConfigurationProviders(configurationProviders);
	}
	
	public ConfigurationTypeElement(Element configurationElement, TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		
		super(processingEnv, roundEnv);
		
		this.configurationElement = configurationElement;
		this.domainType = null;
		this.dtoType = null;

		this.canonicalName = configurationElement.getSimpleName().toString();

		this.transferObjectConfiguration = new TransferObjectConfiguration(configurationElement, processingEnv);
		this.configurationProviders = getConfigurationProviders(configurationProviders);
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return processingEnv.getTypeUtils().toMutableType((DeclaredType)configurationElement.asType());
	}

	void setConfigurationProviders(ConfigurationProvider[] configurationProviders) {
		this.configurationProviders = configurationProviders;
	}
	
	public boolean isValid() {
		if (transferObjectConfiguration == null) {
			return false;
		}
		
		return transferObjectConfiguration.isValid();
	}
	
	private ConverterTypeElement getConverter(TransferObjectConfiguration transferObjectConfiguration) {
		
		if (!transferObjectConfiguration.isValid()) {
			return null;
		}
		
		TypeElement converter = transferObjectConfiguration.getConverter();
		if (converter != null) {
			return new ConverterTypeElement(this, converter, processingEnv, roundEnv);
		}
				
		Element configurationElement = asElement();
		
		if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
			return null;
		}

		return new ConverterTypeElement(this, processingEnv, roundEnv);
	}

	public DomainTypeElement getDomain() {
		if (!domainTypeElementInitialized) {
			MutableDeclaredType domainType = null;
			
			if (this.domainType != null) {
				domainType = this.domainType;
			} else {
				TypeElement domainTypeElement = transferObjectConfiguration.getEvaluatedDomainType();
				if (domainTypeElement != null) {
					domainType = processingEnv.getTypeUtils().toMutableType((DeclaredType) domainTypeElement.asType());
				}
			}
			
			if (domainType != null) {
				this.domainTypeElement = new DomainTypeElement(domainType, dtoType, this, processingEnv, roundEnv, configurationProviders);
			} else {
				if (this.domainType == null) {
					TypeElement domainInterface = transferObjectConfiguration.getDomainInterface();
					if (domainInterface != null) {
						if (dtoType != null && ProcessorUtils.implementsType(dtoType, domainInterface.asType())) {
							this.domainTypeElement = new DomainTypeElement(null, dtoType, this, processingEnv, roundEnv, configurationProviders);
						} else {
							this.domainTypeElement = new DomainTypeElement(processingEnv.getTypeUtils().toMutableType(domainInterface.asType()), dtoType, this, processingEnv, roundEnv, configurationProviders);
							this.domainTypeElement.setInterface(true);
						}
					}
				}
			}

			if (this.domainTypeElement == null) {
				this.dtoTypeElementInitialized = true;
				this.dtoTypeElement = null;
			} else {
				domainTypeElement.prefixTypeParameter(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX);
			}
			
			this.domainTypeElementInitialized = true;
		}
		return domainTypeElement;
	}
	
	public DtoDeclaredType getDto() {
		
		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getDto();
		}

		if (!dtoTypeElementInitialized) {
			
			DeclaredType dtoType = null;
			
			if (this.dtoType != null) {
				dtoType = this.dtoType;
			} else {
				if (transferObjectConfiguration.isValid()) {
					TypeElement dtoTypeElement = transferObjectConfiguration.getDto();
					if (dtoTypeElement != null) {
						dtoType = (DeclaredType) dtoTypeElement.asType();
					}
				}
			}
			
			if (dtoType != null) {
				this.dtoTypeElement = new DtoDeclared(this, dtoType, processingEnv, roundEnv, configurationProviders);
			} else {
				if (this.dtoType == null && transferObjectConfiguration.isValid()) {
					TypeElement dtoInterface = transferObjectConfiguration.getDtoInterface();
					if (dtoInterface != null) {
						if (domainType != null && processingEnv.getTypeUtils().implementsType(domainType, processingEnv.getTypeUtils().toMutableType(dtoInterface.asType()))) {
							this.dtoTypeElement = new DtoDeclared(this, domainType, processingEnv, roundEnv, configurationProviders);
						} else {
							this.dtoTypeElement = new DtoDeclared(this, processingEnv.getTypeUtils().toMutableType((DeclaredType) dtoInterface.asType()), processingEnv, roundEnv, configurationProviders);
							this.dtoTypeElement.setInterface(true);
						}
					}
				}

				if (this.dtoTypeElement == null) {
					Element configurationElement = asElement();
					
					if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
						return null;
					}

					this.dtoTypeElement = new DtoDeclared(this, processingEnv, roundEnv, configurationProviders);
				}
			}

			if (dtoTypeElement != null) {
				dtoTypeElement.prefixTypeParameter(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX);
			}

			this.dtoTypeElementInitialized = true;
		}
		
		return dtoTypeElement;
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
	
	public Element asElement() {
		return configurationElement;
	}
	
	public ConfigurationTypeElement getDelegateConfigurationTypeElement() {
		if (!delegateConfigurationTypeElementInitialized) {
			if (transferObjectConfiguration.getConfiguration() != null) {
				this.delegateConfigurationTypeElement = new ConfigurationTypeElement(transferObjectConfiguration.getConfiguration(), processingEnv, roundEnv);
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
		
		return transferObjectConfiguration.getMappingForDto((MutableDeclaredType)dtoType) != null;
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

		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(asElement().getEnclosedElements());

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