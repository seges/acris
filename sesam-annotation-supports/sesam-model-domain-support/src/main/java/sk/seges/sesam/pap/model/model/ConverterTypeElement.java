package sk.seges.sesam.pap.model.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.model.api.GeneratedClass;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.shared.model.converter.BasicCachedConverter;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class ConverterTypeElement extends TomBaseElement implements GeneratedClass {

	public static final String DEFAULT_SUFFIX = "Converter";	
	public static final String DEFAULT_CONFIGURATION_SUFFIX = "Configuration";
	public static final String DTO_TYPE_ARGUMENT_PREFIX = "DTO";
	public static final String DOMAIN_TYPE_ARGUMENT_PREFIX = "DOMAIN";

	private final TypeElement converterTypeElement;
	private final ConfigurationTypeElement configurationTypeElement;
	
	private final boolean generated;
	
	ConverterTypeElement(ConfigurationTypeElement configurationTypeElement, TypeElement converterTypeElement, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		this.converterTypeElement = converterTypeElement;
		this.configurationTypeElement = configurationTypeElement;
		this.generated = false;
		setDelegateImmutableType(getNameTypesUtils().toImmutableType(converterTypeElement));
		
		initialize();
	}

	ConverterTypeElement(ConfigurationTypeElement configurationTypeElement, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		super(processingEnv, roundEnv);
		this.generated = true;
		this.converterTypeElement = null;
		this.configurationTypeElement = configurationTypeElement;
		setDelegateImmutableType(getGeneratedConverterTypeFromConfiguration(configurationTypeElement));
	}

	private void initialize() {
		if (typeParametersSupport.hasTypeParameters(this) && typeParametersSupport.hasTypeParameters(configurationTypeElement.getDomainTypeElement())) {

			TypeParameter[] converterParameters = new TypeParameter[configurationTypeElement.getDomainTypeElement().getTypeParameters().length * 2];
			
			int i = 0;
			
			for (TypeParameter typeParameter: configurationTypeElement.getDtoTypeElement().getTypeParameters()) {
				converterParameters[i] = typeParameter;
				i++;
			}

			for (TypeParameter typeParameter: configurationTypeElement.getDomainTypeElement().getTypeParameters()) {
				converterParameters[i] = typeParameter;
				i++;
			}
			
			setDelegateImmutableType(TypedClassBuilder.get(this, converterParameters));
		}
	}
	
	protected PackageValidatorProvider getPackageValidationProvider() {
		return new DefaultPackageValidatorProvider();
	}

	private List<TypeParameter> copyTypeArguments(String prefix, DeclaredType declaredType, HasTypeParameters referenceType) {
		
		List<TypeParameter> result = new ArrayList<TypeParameter>();
		
		for (int i = 0; i < declaredType.getTypeArguments().size(); i++) {
			TypeParameter typeParameter = referenceType.getTypeParameters()[i];
			
			String name = typeParameter.getSimpleName();
			
			if (name != null && name.length() > 0) {
				result.add(TypeParameterBuilder.get(prefix + "_" + name, typeParameter.getBounds()));
			}
			//TODO handle else ???
		}
		
		return result;
	}

	private ImmutableType getGeneratedConverterTypeFromConfiguration(ConfigurationTypeElement configurationTypeElement) {

		Element configurationElement = configurationTypeElement.asElement();
		
		if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
			return null;
		}
		
		DeclaredType declaredType = (DeclaredType)configurationTypeElement.asElement().asType();
		
		TransferObjectConfiguration transferObjectConfiguration = new TransferObjectConfiguration((TypeElement)declaredType.asElement(), processingEnv);
		
		TypeElement converter = transferObjectConfiguration.getConverter();
		if (converter != null) {
			return null;
		}

		TypeElement domainType = transferObjectConfiguration.getDomain();
		
		ImmutableType configurationNameType = getNameTypesUtils().toImmutableType(configurationTypeElement.asElement());
		
		//Remove configuration suffix if it is there - just to have better naming convention
		String simpleName = configurationNameType.getSimpleName();
		if (simpleName.endsWith( DEFAULT_CONFIGURATION_SUFFIX) && simpleName.length() > DEFAULT_CONFIGURATION_SUFFIX.length()) {
			simpleName = simpleName.substring(0, simpleName.lastIndexOf(DEFAULT_CONFIGURATION_SUFFIX));
			configurationNameType = configurationNameType.setName(simpleName);
		}
		
		configurationNameType = configurationNameType.addClassSufix(DEFAULT_SUFFIX);
		
		NameTypesUtils nameTypes = new NameTypesUtils(processingEnv.getElementUtils());
		
		if (domainType.getTypeParameters().size() > 0) {
			
			HasTypeParameters referenceType = (HasTypeParameters)nameTypes.toType(domainType);
			
			//there are type parameters, so they should be passed into the converter definition itself			
			List<TypeParameter> typeParameters = copyTypeArguments(DTO_TYPE_ARGUMENT_PREFIX, (DeclaredType)domainType.asType(), referenceType);
			typeParameters.addAll(copyTypeArguments(DOMAIN_TYPE_ARGUMENT_PREFIX, (DeclaredType)domainType.asType(), referenceType));

			configurationNameType = nameTypes.erasure(configurationNameType);

			return TypedClassBuilder.get(configurationNameType, typeParameters.toArray(new TypeParameter[] {}));
		}
		
		return configurationNameType;
	}
	
	@Override
	public boolean isGenerated() {
		return generated;
	}
	
	public TypeElement asElement() {
		return converterTypeElement;
	}
	
	public ConfigurationTypeElement getConfigurationTypeElement() {
		return configurationTypeElement;
	}

	private ConverterParameter toConverterParameter(ParameterElement parameter) {
		ConverterParameter converterParameter = new ConverterParameter();
		converterParameter.setType(parameter.getType());
		converterParameter.setName(parameter.getName());
		converterParameter.setConverter(this);
		converterParameter.setConverter(parameter.isConverter());
		return converterParameter;
	}
	
	private ConverterParameter toConverterParameter(VariableElement constructorParameter) {
		ConverterParameter converterParameter = new ConverterParameter();
		TypeElement dtoConverterTypeElement = processingEnv.getElementUtils().getTypeElement(DtoConverter.class.getCanonicalName());
		converterParameter.setConverter(ProcessorUtils.implementsType(constructorParameter.asType(), dtoConverterTypeElement.asType()));
		converterParameter.setType(getNameTypesUtils().toImmutableType(constructorParameter.asType()));
		converterParameter.setName(constructorParameter.getSimpleName().toString());
		converterParameter.setConverter(this);
		return converterParameter;
	}
	
	public List<ConverterParameter> getConverterParameters(ParametersResolver parametersResolver) {

		List<ConverterParameter> parameters = new LinkedList<ConverterParameter>();

		if (!isGenerated()) {
			TypeElement converterTypeElement = processingEnv.getElementUtils().getTypeElement(getCanonicalName());
			List<ExecutableElement> constructors = ElementFilter.constructorsIn(converterTypeElement.getEnclosedElements());

			if (constructors != null && constructors.size() > 0) {
				//Take the last one
				List<? extends VariableElement> constructorParameters = constructors.get(constructors.size() - 1).getParameters();

				for (VariableElement constructorParameter : constructorParameters) {
					parameters.add(toConverterParameter(constructorParameter));
				}
			}
		} else {
			TypeElement cachedConverterType = processingEnv.getElementUtils().getTypeElement(BasicCachedConverter.class.getCanonicalName());
			List<ExecutableElement> constructors = ElementFilter.constructorsIn(cachedConverterType.getEnclosedElements());

			if (constructors != null && constructors.size() > 0) {
				List<? extends VariableElement> constructorParameters = constructors.get(constructors.size() - 1).getParameters();

				for (VariableElement constructorParameter : constructorParameters) {
					parameters.add(toConverterParameter(constructorParameter));
				}
			}

			ParameterElement[] constructorAditionalParameters = parametersResolver.getConstructorAditionalParameters(configurationTypeElement.getDomainTypeElement().asType());

			for (ParameterElement constructorAditionalParameter: constructorAditionalParameters) {
				parameters.add(toConverterParameter(constructorAditionalParameter));
			}
		}

		return parameters;
	}
}