package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.builder.api.TypeMirrorConverter;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.api.GeneratedClass;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

public class DtoTypeElement extends TomBaseElement implements GeneratedClass {

	private final ConfigurationTypeElement configurationTypeElement;

	private final boolean generated;
	private final TypeMirror dtoType;
	private final TypeParametersSupport typeParametersSupport;
	
	private boolean isInterface = false;
	
	DtoTypeElement(ConfigurationTypeElement configurationTypeElement, TypeElement dtoTypeElement, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		this.configurationTypeElement = configurationTypeElement;
		this.generated = false;
		setDelegateImmutableType(getNameTypesUtils().toImmutableType(dtoTypeElement));
		this.dtoType = dtoTypeElement.asType();
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, getNameTypesUtils());
		initialize(processingEnv);
	}

	DtoTypeElement(ConfigurationTypeElement configurationTypeElement, DeclaredType dtoType, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		this.configurationTypeElement = configurationTypeElement;
		this.generated = false;
		setDelegateImmutableType(getNameTypesUtils().toImmutableType(dtoType));
		this.dtoType = null;
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, getNameTypesUtils());
		initialize(processingEnv);
	}

	DtoTypeElement(ConfigurationTypeElement configurationTypeElement, ImmutableType dtoType, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		this.configurationTypeElement = configurationTypeElement;
		this.generated = false;
		setDelegateImmutableType(dtoType);
		this.dtoType = null;
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, getNameTypesUtils());
		initialize(processingEnv);
	}

	DtoTypeElement(ConfigurationTypeElement configurationTypeElement, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		this.configurationTypeElement = configurationTypeElement;
		this.generated = true;
		this.dtoType = null;
		setDelegateImmutableType(getGeneratedDtoTypeFromConfiguration(configurationTypeElement));
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, getNameTypesUtils());
		initialize(processingEnv);
	}

	public DtoTypeElement(TypeElement configurationHolderTypeElement, TypeMirror dtoType, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);

		TransferObjectConfiguration transferObjectConfiguration = new TransferObjectConfiguration(configurationHolderTypeElement, processingEnv);
		this.dtoType = dtoType;
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, getNameTypesUtils());
		
		if (dtoType.getKind().equals(TypeKind.DECLARED)) {
			TransferObjectMapping dtoMapping = transferObjectConfiguration.getMappingForDto((DeclaredType)dtoType);
			if (dtoMapping == null) {
				transferObjectConfiguration = new TransferObjectConfiguration(((DeclaredType)dtoType).asElement(), processingEnv);
			} else {
				transferObjectConfiguration.setReferenceMapping(dtoMapping);
			}
		} else {
			transferObjectConfiguration = null;
		}

		if (transferObjectConfiguration != null) {
			this.configurationTypeElement = new ConfigurationTypeElement(transferObjectConfiguration, processingEnv, roundEnv);
		} else {
			this.configurationTypeElement = toHelper.getConfigurationForDto(dtoType);
		}
		
		setDelegateImmutableType(new NameTypesUtils(processingEnv.getElementUtils()).toImmutableType(dtoType));
		this.generated = false;
				
		initialize(processingEnv);
	}

	public DtoTypeElement(TypeMirror dtoType, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		this.dtoType = dtoType;
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, getNameTypesUtils());

		this.configurationTypeElement = toHelper.getConfigurationForDto(dtoType);

		setDelegateImmutableType(new NameTypesUtils(processingEnv.getElementUtils()).toImmutableType(dtoType));
		this.generated = false;
				
		initialize(processingEnv);
	}
	
	class TypeMirrorDtoHandler implements TypeMirrorConverter {

		@Override
		public NamedType handleType(TypeMirror type) {
			return toHelper.convertType(type);
		}	
	}
	
	class NameTypesDtoUtils extends NameTypesUtils {

		public NameTypesDtoUtils(Elements elements) {
			super(elements);
		}
		
		@Override
		protected TypeMirrorConverter getTypeMirrorConverter() {
			return new TypeMirrorDtoHandler();
		}
	}

	@Override
	protected NameTypesDtoUtils getNameTypesUtils() {
		return new NameTypesDtoUtils(processingEnv.getElementUtils());
	}

	private void initialize(ProcessingEnvironment processingEnv) {
		if (configurationTypeElement == null) {
			return;
		}
//		//TODO I'm not sure if this is good for something
//		setDelegateImmutableType(new TypeParametersSupport(processingEnv, new NameTypesUtils(processingEnv.getElementUtils())).
//				applyVariableTypeParameters(getDelegateImmutableType(), configurationTypeElement.getDomainTypeElement().asType()));
		if (typeParametersSupport.hasTypeParameters(configurationTypeElement.getDomainTypeElement())) {
			NamedType type = getNameTypesUtils().toType(configurationTypeElement.getDomainTypeElement().asType());
			setDelegateImmutableType(TypedClassBuilder.get(getDelegateImmutableType(), ((HasTypeParameters)type).getTypeParameters()));
		}
	}
	
	protected PackageValidatorProvider getPackageValidationProvider() {
		return new DefaultPackageValidatorProvider();
	}

	private ImmutableType getGeneratedDtoTypeFromConfiguration(ConfigurationTypeElement configurationType) {

		Element configurationElement = configurationType.asElement();
		
		if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
			return null;
		}

		ImmutableType configurationNameType = new NameTypesUtils(processingEnv.getElementUtils()).toImmutableType(configurationTypeElement.asElement());

		PackageValidator packageValidator = getPackageValidationProvider().get(configurationNameType)
				.moveTo(LocationType.SHARED).moveTo(LayerType.MODEL).clearType().moveTo(ImplementationType.DTO);
		configurationNameType = configurationNameType.changePackage(packageValidator);
		return configurationNameType.getSimpleName().endsWith(TransferObjectHelper.DEFAULT_SUFFIX) ? configurationNameType.setName(configurationNameType
				.getSimpleName().substring(0, configurationNameType.getSimpleName().length() - TransferObjectHelper.DEFAULT_SUFFIX.length()))
				: configurationNameType.addClassSufix(TransferObjectHelper.DTO_SUFFIX);
	}

	@Override
	public boolean isGenerated() {
		return generated;
	}
	
	public DomainTypeElement getDomainTypeElement() {
		DomainTypeElement result = null;
		if (configurationTypeElement != null) {
			result = configurationTypeElement.getDomainTypeElement();
		}
		
		if (result != null) {
			return result;
		}

		if (dtoType != null) {
			return new DomainTypeElement(dtoType, processingEnv, roundEnv);
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
		
		return configurationTypeElement.getConverterTypeElement();
	}
}