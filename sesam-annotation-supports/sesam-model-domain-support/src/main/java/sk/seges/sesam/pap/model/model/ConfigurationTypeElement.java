package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

public class ConfigurationTypeElement extends TomBase {

	private DtoTypeElement dtoTypeElement;
	private boolean dtoTypeElementInitialized = false;

	private DomainTypeElement domainTypeElement;
	private boolean domainTypeElementInitialized = false;
	
	private ConfigurationTypeElement delegateConfigurationTypeElement;
	private boolean delegateConfigurationTypeElementInitialized = false;

	private ConverterTypeElement converterTypeElement;
	private boolean converterTypeElementInitialized = false;

	private final Element configurationElement;
	private final TransferObjectConfiguration transferObjectConfiguration;

	private final DeclaredType domainType;
	private final DeclaredType dtoType;

	private final String canonicalName;
	private ConfigurationProvider[] configurationProviders;
	
	public ConfigurationTypeElement(DeclaredType domainType, DeclaredType dtoType, TypeElement configurationElement, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		super(processingEnv, roundEnv);
		
		this.configurationElement = configurationElement;
		this.domainType = domainType;
		this.dtoType = dtoType;
		
		this.canonicalName = configurationElement.getQualifiedName().toString();
//		setDelegateImmutableType(getNameTypesUtils().toImmutableType(configurationElement));
		
		this.transferObjectConfiguration = new TransferObjectConfiguration(configurationElement, processingEnv);
		if (this.transferObjectConfiguration.getReferenceMapping() == null) {
			this.transferObjectConfiguration.setReferenceMapping(transferObjectConfiguration.getMappingForDto(dtoType));
		}
		this.configurationProviders = getConfigurationProviders(configurationProviders);
	}
	
	public ConfigurationTypeElement(Element configurationElement, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		
		super(processingEnv, roundEnv);
		
		this.configurationElement = configurationElement;
		this.domainType = null;
		this.dtoType = null;

		this.canonicalName = configurationElement.getSimpleName().toString();

		this.transferObjectConfiguration = new TransferObjectConfiguration(configurationElement, processingEnv);
		this.configurationProviders = getConfigurationProviders(configurationProviders);
	}

	ConfigurationTypeElement(DeclaredType domainType, DeclaredType dtoType, TransferObjectConfiguration transferObjectConfiguration, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, ConfigurationProvider... configurationProviders) {
		
		super(processingEnv, roundEnv);
		
		this.configurationElement = transferObjectConfiguration.getConfigurationHolderElement();
		this.transferObjectConfiguration = transferObjectConfiguration;
		this.domainType = domainType;
		this.dtoType = dtoType;

		this.canonicalName = configurationElement.getSimpleName().toString();
		this.configurationProviders = getConfigurationProviders(configurationProviders);
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

	public DomainTypeElement getDomainTypeElement() {
		if (!domainTypeElementInitialized) {
			DeclaredType domainType = null;
			
			if (this.domainType != null) {
				domainType = this.domainType;
			} else {
				TypeElement domainTypeElement = transferObjectConfiguration.getEvaluatedDomainType();
				if (domainTypeElement != null) {
					domainType = (DeclaredType) domainTypeElement.asType();
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
							this.domainTypeElement = new DomainTypeElement(domainInterface.asType(), dtoType, this, processingEnv, roundEnv, configurationProviders);
							this.domainTypeElement.setInterface(true);
						}
					}
				}
			}

			if (this.domainTypeElement == null) {
				this.dtoTypeElementInitialized = true;
				this.dtoTypeElement = null;
			}

			this.domainTypeElementInitialized = true;
		}
		return domainTypeElement;
	}
	
	public DtoTypeElement getDtoTypeElement() {
		
		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getDtoTypeElement();
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
				this.dtoTypeElement = new DtoTypeElement(this, dtoType, processingEnv, roundEnv, configurationProviders);
			} else {
				if (this.dtoType == null && transferObjectConfiguration.isValid()) {
					TypeElement dtoInterface = transferObjectConfiguration.getDtoInterface();
					if (dtoInterface != null) {
						if (domainType != null && ProcessorUtils.implementsType(domainType, dtoInterface.asType())) {
							this.dtoTypeElement = new DtoTypeElement(this, domainType, processingEnv, roundEnv, configurationProviders);
						} else {
							this.dtoTypeElement = new DtoTypeElement(this, dtoInterface, processingEnv, roundEnv, configurationProviders);
							this.dtoTypeElement.setInterface(true);
						}
					}
				}

				if (this.dtoTypeElement == null) {
					Element configurationElement = asElement();
					
					if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
						return null;
					}

					this.dtoTypeElement = new DtoTypeElement(this, processingEnv, roundEnv, configurationProviders);
				}
			}

			//this.dtoTypeElement = getDto(transferObjectConfiguration);
			this.dtoTypeElementInitialized = true;
		}
		
		return dtoTypeElement;
	}
	
	public ConverterTypeElement getConverterTypeElement() {

		if (!converterTypeElementInitialized) {
			this.converterTypeElement = getConverter(transferObjectConfiguration);
			this.converterTypeElementInitialized = true;
		}
		
		if (converterTypeElement != null) {
			if (converterTypeElement.isGenerated()) {
				if (getDelegateConfigurationTypeElement() != null) {
					ConverterTypeElement delegateConverterTypeElement = getDelegateConfigurationTypeElement().getConverterTypeElement();
					if (delegateConverterTypeElement != null) {
						return delegateConverterTypeElement;
					}
				}
			}
			return converterTypeElement;
		}

		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getConverterTypeElement();
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
	
	public boolean appliesForDomainType(TypeMirror domainType) {
		
		//Configuration should be applied only for declared types like classes or interfaces
		if (!domainType.getKind().equals(TypeKind.DECLARED)) {
			return false;
		}
		
		return transferObjectConfiguration.getMappingForDomain((DeclaredType)domainType) != null;
	}

	public boolean appliesForDtoType(TypeMirror dtoType) {

		//Configuration should be applied only for declared types like classes or interfaces
		if (!dtoType.getKind().equals(TypeKind.DECLARED)) {
			return false;
		}
		
		return transferObjectConfiguration.getMappingForDto((DeclaredType)dtoType) != null;
	}

	public String getCanonicalName() {
		return canonicalName;
	}
}