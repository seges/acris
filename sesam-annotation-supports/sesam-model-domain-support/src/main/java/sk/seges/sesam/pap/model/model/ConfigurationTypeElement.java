package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.utils.ProcessorUtils;

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
	
	public ConfigurationTypeElement(DeclaredType domainType, DeclaredType dtoType, TypeElement configurationElement, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		
		this.configurationElement = configurationElement;
		this.domainType = domainType;
		this.dtoType = dtoType;
		
		this.canonicalName = configurationElement.getQualifiedName().toString();
//		setDelegateImmutableType(getNameTypesUtils().toImmutableType(configurationElement));
		
		this.transferObjectConfiguration = new TransferObjectConfiguration(configurationElement, processingEnv);
	}
	
	public ConfigurationTypeElement(Element configurationElement, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		
		super(processingEnv, roundEnv);
		
		this.configurationElement = configurationElement;
		this.domainType = null;
		this.dtoType = null;

		this.canonicalName = configurationElement.getSimpleName().toString();

		this.transferObjectConfiguration = new TransferObjectConfiguration(configurationElement, processingEnv);
	}

	public ConfigurationTypeElement(TransferObjectConfiguration transferObjectConfiguration, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		
		super(processingEnv, roundEnv);
		
		this.configurationElement = transferObjectConfiguration.getConfigurationHolderElement();
		this.transferObjectConfiguration = transferObjectConfiguration;
		this.domainType = null;
		this.dtoType = null;

		this.canonicalName = configurationElement.getSimpleName().toString();
	}

//	private DtoTypeElement getDto(TransferObjectConfiguration transferObjectConfiguration) {
//
//		if (!transferObjectConfiguration.isValid()) {
//			return null;
//		}
//
//		TypeElement dto = transferObjectConfiguration.getDto();
//		if (dto != null) {
//			return new DtoTypeElement(this, dto, processingEnv, roundEnv);
//		}
//
//		TypeElement dtoInterface = transferObjectConfiguration.getDtoInterface();
//		if (dtoInterface != null) {
//			if (ProcessorUtils.implementsType(domainType, dtoInterface.asType())) {
//				return new DtoTypeElement(this, domainType, processingEnv, roundEnv);
//			}
//			DtoTypeElement result = new DtoTypeElement(this, dtoInterface, processingEnv, roundEnv);
//			result.setInterface(true);
//			return result;
//		} 
//
//		Element configurationElement = asElement();
//		
//		if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
//			return null;
//		}
//
//		return new DtoTypeElement(this, processingEnv, roundEnv);
//	}

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
				this.domainTypeElement = new DomainTypeElement(this, domainType, processingEnv, roundEnv);
			} else {
				if (this.domainType == null) {
					TypeElement domainInterface = transferObjectConfiguration.getDomainInterface();
					if (domainInterface != null) {
						this.domainTypeElement = new DomainTypeElement(this,(DeclaredType) domainInterface.asType(), processingEnv, roundEnv);
						this.domainTypeElement.setInterface(true);
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
				this.dtoTypeElement = new DtoTypeElement(this, dtoType, processingEnv, roundEnv);
			} else {
				if (this.dtoType == null && transferObjectConfiguration.isValid()) {
					TypeElement dtoInterface = transferObjectConfiguration.getDtoInterface();
					if (dtoInterface != null) {
						if (domainType != null && ProcessorUtils.implementsType(domainType, dtoInterface.asType())) {
							this.dtoTypeElement = new DtoTypeElement(this, domainType, processingEnv, roundEnv);
						} else {
							this.dtoTypeElement = new DtoTypeElement(this, dtoInterface, processingEnv, roundEnv);
							this.dtoTypeElement.setInterface(true);
						}
					}
				}

				if (this.dtoTypeElement == null) {
					Element configurationElement = asElement();
					
					if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
						return null;
					}

					this.dtoTypeElement = new DtoTypeElement(this, processingEnv, roundEnv);
				}
			}

			//this.dtoTypeElement = getDto(transferObjectConfiguration);
			this.dtoTypeElementInitialized = true;
		}
		
		return dtoTypeElement;
	}
	
	public ConverterTypeElement getConverterTypeElement() {

		if (getDelegateConfigurationTypeElement() != null) {
			return getDelegateConfigurationTypeElement().getConverterTypeElement();
		}

		if (!converterTypeElementInitialized) {
			this.converterTypeElement = getConverter(transferObjectConfiguration);
			this.converterTypeElementInitialized = true;
		}
		
		if (converterTypeElement != null) {
			return converterTypeElement;
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
		
		if (getDomainTypeElement() == null || !domainType.getKind().equals(TypeKind.DECLARED)) {
			return false;
		}
		
		if (getDomainTypeElement().isInterface()) {
			if (ProcessorUtils.implementsType(domainType, getDomainTypeElement().asType())) {
				return true;
			}
		} else {
			if (processingEnv.getTypeUtils().erasure(getDomainTypeElement().asType()).equals(
				processingEnv.getTypeUtils().erasure(domainType))) {
				return true;
			}
		}
		
		return false;
	}

	public boolean appliesForDtoType(TypeMirror dtoType) {
		
		if (getDtoTypeElement() == null || !dtoType.getKind().equals(TypeKind.DECLARED)) {
			return false;
		}
		
		if (getDtoTypeElement().isInterface()) {
			if (ProcessorUtils.implementsType(dtoType, getDtoTypeElement().asType())) {
				return true;
			}
		} else {
			if (processingEnv.getTypeUtils().erasure(getDtoTypeElement().asType()).equals(
				processingEnv.getTypeUtils().erasure(dtoType))) {
				return true;
			}
		}
		
		return false;
	}

	public String getCanonicalName() {
		return canonicalName;
	}
}