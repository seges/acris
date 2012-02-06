package sk.seges.sesam.pap.model.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.pap.model.model.api.GeneratedClass;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.shared.model.converter.BasicCachedConverter;
import sk.seges.sesam.shared.model.converter.api.InstantiableDtoConverter;

public class ConverterTypeElement extends TomBaseDeclaredType implements GeneratedClass {

	public static final String DEFAULT_SUFFIX = "Converter";	
	public static final String DEFAULT_CONFIGURATION_SUFFIX = "Configuration";
	public static final String DTO_TYPE_ARGUMENT_PREFIX = "DTO";
	public static final String DOMAIN_TYPE_ARGUMENT_PREFIX = "DOMAIN";

	private final TypeElement converterTypeElement;
	private final ConfigurationTypeElement configurationTypeElement;
	
	private MutableDeclaredType converterBase;
	
	private final boolean generated;
	
	ConverterTypeElement(ConfigurationTypeElement configurationTypeElement, TypeElement converterTypeElement, EnvironmentContext<TransferObjectProcessingEnvironment> envContext) {
		super(envContext);
		this.converterTypeElement = converterTypeElement;
		this.configurationTypeElement = configurationTypeElement;
		this.generated = false;
		
		initialize();
	}
	
	public ConverterTypeElement(ConfigurationTypeElement configurationTypeElement, EnvironmentContext<TransferObjectProcessingEnvironment> envContext) {
		super(envContext);
		this.generated = true;
		this.converterTypeElement = null;
		this.configurationTypeElement = configurationTypeElement;

		setKind(MutableTypeKind.CLASS);
		setSuperClass(getTypeUtils().getDeclaredType(
				(MutableDeclaredType) getTypeUtils().toMutableType(BasicCachedConverter.class), configurationTypeElement.getDto(), configurationTypeElement.getDomain()));
	}

	public boolean isConverterInstantiable() {
		return getTypeUtils().implementsType(this, getTypeUtils().toMutableType(InstantiableDtoConverter.class));
	}

	public MutableDeclaredType getConverterBase() {

		if (ensureDelegateType().hasTypeParameters() || !isConverterInstantiable()) {
			return ensureDelegateType();
		}
		
		if (converterBase != null) {
			return converterBase;
		}
		
		MutableTypes typeUtils = getTypeUtils();

		DomainType domain = getDomain();

		converterBase = typeUtils.getDeclaredType(typeUtils.toMutableType(InstantiableDtoConverter.class), typeUtils.getTypeVariable(null, domain.getDto()), typeUtils.getTypeVariable(null, domain));	
		
		return converterBase;
	}
	
	protected MutableDeclaredType getDelegate() {
		if (converterTypeElement != null) {
			return (MutableDeclaredType) getTypeUtils().toMutableType((DeclaredType)converterTypeElement.asType());
		}
		return getGeneratedConverterTypeFromConfiguration(configurationTypeElement);
	}

	private void initialize() {
		if (this.hasVariableParameterTypes() && configurationTypeElement.getDomain().hasTypeParameters()) {

			MutableTypeVariable[] converterParameters = new MutableTypeVariable[configurationTypeElement.getDomain().getTypeVariables().size() * 2];
			
			int i = 0;
			
			for (MutableTypeVariable typeVariable: configurationTypeElement.getDto().getTypeVariables()) {
				converterParameters[i] = typeVariable;
				i++;
			}

			for (MutableTypeVariable typeVariable: configurationTypeElement.getDomain().getTypeVariables()) {
				converterParameters[i] = typeVariable;
				i++;
			}
			
			setDelegate(this.clone().setTypeVariables(converterParameters));
		}
	}
	
	protected PackageValidatorProvider getPackageValidationProvider() {
		return new DefaultPackageValidatorProvider();
	}

	private List<MutableTypeVariable> prefixTypeArguments(String prefix, DeclaredType declaredType, MutableDeclaredType referenceType) {
		
		List<MutableTypeVariable> result = new ArrayList<MutableTypeVariable>();
		
		Iterator<? extends MutableTypeVariable> iterator = referenceType.getTypeVariables().iterator();
		
		int i = 0;
		while (iterator.hasNext()) {
			if (i >= declaredType.getTypeArguments().size()) {
				break;
			}
			i++;
			MutableTypeVariable typeParameter = iterator.next();
			
			String name = typeParameter.getVariable();
			
			if (name != null && name.length() > 0) {
				result.add(typeParameter.clone().setVariable(prefix + "_" + name));
			}
		}
		
		return result;
	}

	private MutableDeclaredType getGeneratedConverterTypeFromConfiguration(ConfigurationTypeElement configurationTypeElement) {

		Element configurationElement = configurationTypeElement.asConfigurationElement();
		
		if (!configurationElement.asType().getKind().equals(TypeKind.DECLARED)) {
			return null;
		}
		
		DeclaredType declaredType = (DeclaredType)configurationTypeElement.asConfigurationElement().asType();
		
		TransferObjectMappingAccessor transferObjectConfiguration = new TransferObjectMappingAccessor((TypeElement)declaredType.asElement(), environmentContext.getProcessingEnv());
		
		TypeElement converter = transferObjectConfiguration.getConverter();
		if (converter != null || !configurationTypeElement.ensureDelegateType().getKind().isDeclared()) {
			return null;
		}

		TypeElement domainType = transferObjectConfiguration.getDomain();
		
		//We are going to modify simple name, so clone is necessary
		MutableDeclaredType configurationNameType = ((MutableDeclaredType)configurationTypeElement.ensureDelegateType()).clone();
		
		//Remove configuration suffix if it is there - just to have better naming convention
		String simpleName = configurationNameType.getSimpleName();
		if (simpleName.endsWith( DEFAULT_CONFIGURATION_SUFFIX) && simpleName.length() > DEFAULT_CONFIGURATION_SUFFIX.length()) {
			simpleName = simpleName.substring(0, simpleName.lastIndexOf(DEFAULT_CONFIGURATION_SUFFIX));
			configurationNameType = configurationNameType.setSimpleName(simpleName);
		}
		
		MutableDeclaredType converterNameType = configurationNameType.addClassSufix(DEFAULT_SUFFIX);
		
		if (domainType.getTypeParameters().size() > 0) {
			
			MutableDeclaredType referenceType = getTypeUtils().toMutableType((DeclaredType)domainType.asType());
			
			//there are type parameters, so they should be passed into the converter definition itself
			List<MutableTypeVariable> typeParameters = prefixTypeArguments(DTO_TYPE_ARGUMENT_PREFIX, (DeclaredType)domainType.asType(), referenceType);
			typeParameters.addAll(prefixTypeArguments(DOMAIN_TYPE_ARGUMENT_PREFIX, (DeclaredType)domainType.asType(), referenceType));

			converterNameType.setTypeVariables(typeParameters.toArray(new MutableTypeVariable[] {}));
		}
		
		return converterNameType;
	}
	
	@Override
	public boolean isGenerated() {
		return generated;
	}
	
	public TypeElement asElement() {
		return converterTypeElement;
	}
	
	public ConfigurationTypeElement getConfiguration() {
		return configurationTypeElement;
	}

	private ConverterParameter toConverterParameter(ParameterElement parameter) {
		ConverterParameter converterParameter = new ConverterParameter();
		converterParameter.setType(parameter.getType());
		converterParameter.setName(parameter.getName());
//		converterParameter.setConverter(this);
//		converterParameter.setConverter(parameter.isConverter());
		converterParameter.setPropagated(parameter.isPropagated());
		return converterParameter;
	}
	
	private ConverterParameter toConverterParameter(VariableElement constructorParameter) {
		ConverterParameter converterParameter = new ConverterParameter();
//		TypeElement dtoConverterTypeElement = processingEnv.getElementUtils().getTypeElement(DtoConverter.class.getCanonicalName());
//		converterParameter.setConverter(ProcessorUtils.implementsType(constructorParameter.asType(), dtoConverterTypeElement.asType()));
		converterParameter.setType(getTypeUtils().toMutableType(constructorParameter.asType()));
		converterParameter.setName(constructorParameter.getSimpleName().toString());
//		converterParameter.setConverter(this);
		converterParameter.setPropagated(true);
		return converterParameter;
	}

	private List<ExecutableElement> getSortedConstructorMethods(TypeElement element) {
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(element.getEnclosedElements());
		Collections.sort(constructors, new Comparator<ExecutableElement>() {

			@Override
			public int compare(ExecutableElement o1, ExecutableElement o2) {
				int size1 = ((ExecutableType)o1.asType()).getParameterTypes().size();
				int size2 = ((ExecutableType)o2.asType()).getParameterTypes().size();
				return size2 - size1;
			}
		});
		
		return constructors;
	}

	public List<ConverterParameter> getConverterParameters(ParametersResolver parametersResolver) {
		return getConverterParameters(parametersResolver, 0);
	}
	
	private List<ConverterParameter> getConstructorParameters(ExecutableElement method) {
		List<ConverterParameter> result = new LinkedList<ConverterParameter>();

		//Strange java bug
		try {
			List<? extends VariableElement> parameters = method.getParameters();
			for (VariableElement parameterElement: parameters) {
				result.add(toConverterParameter(parameterElement));
			}
			
			return result;
		} catch (Exception e) {
		}

		List<? extends TypeMirror> parameterTypes = ((ExecutableType)method.asType()).getParameterTypes();

		
		int i = 0;
		for (TypeMirror parameterType: parameterTypes) {
			ConverterParameter converterParameter = new ConverterParameter();
//			TypeElement dtoConverterTypeElement = processingEnv.getElementUtils().getTypeElement(DtoConverter.class.getCanonicalName());
//			converterParameter.setConverter(ProcessorUtils.implementsType(parameterType, dtoConverterTypeElement.asType()));
			converterParameter.setType(getTypeUtils().toMutableType(parameterType));
			converterParameter.setName("arg" + i++);
//			converterParameter.setConverter(this);
			converterParameter.setPropagated(true);
			result.add(converterParameter);
		}
		
		return result;
	}
	
	public List<ConverterParameter> getConverterParameters(ParametersResolver parametersResolver, int constructorIndex) {

		List<ConverterParameter> parameters = new LinkedList<ConverterParameter>();

		TypeElement converterTypeElement = getElementUtils().getTypeElement(getCanonicalName());

		if (converterTypeElement != null && !isGenerated()) {
			List<ExecutableElement> constructors = getSortedConstructorMethods(converterTypeElement);
			
			ParameterElement[] constructorAditionalParameters = new ParameterElement[0];
			
			if (getConfiguration() != null && getConfiguration().getDomain() != null) {
				constructorAditionalParameters = parametersResolver.getConstructorAditionalParameters();
			}
			
			if (constructors != null) {
				
				List<ConverterParameter> constructorParameters = null;
				
				//TODO
				if (constructorIndex != -1) {
					if (constructors.size() > constructorIndex) {
						constructorParameters = getConstructorParameters(constructors.get(constructorIndex));
					} else {
						constructorParameters = getConstructorParameters(constructors.get(0));
					}
				} else {
					constructorParameters = getConstructorParameters(constructors.get(constructors.size() - 1));
				}
				
				for (ConverterParameter converterParameter : constructorParameters) {
					
					for (ParameterElement constructorAditionalParameter: constructorAditionalParameters) {
						if (!constructorAditionalParameter.isPropagated() &&
								converterParameter.getType().toString(ClassSerializer.CANONICAL, false).equals(
										constructorAditionalParameter.getType().toString(ClassSerializer.CANONICAL, false))) {
							converterParameter.setPropagated(false);
							break;
						}
					}

					parameters.add(converterParameter);
				}
			}
		} else {
			TypeElement cachedConverterType = getElementUtils().getTypeElement(BasicCachedConverter.class.getCanonicalName());
			List<ExecutableElement> constructors = getSortedConstructorMethods(cachedConverterType);

			if (constructors != null && constructors.size() > constructorIndex) {

				List<? extends VariableElement> constructorParameters = null;
				if (constructorIndex != -1) {
					constructorParameters = constructors.get(constructorIndex).getParameters();
				} else {
					constructorParameters = constructors.get(constructors.size() - 1).getParameters();
				}

				for (VariableElement constructorParameter : constructorParameters) {
					parameters.add(toConverterParameter(constructorParameter));
				}
			}

			ParameterElement[] constructorAditionalParameters = parametersResolver.getConstructorAditionalParameters();

			for (ParameterElement constructorAditionalParameter: constructorAditionalParameters) {
				parameters.add(toConverterParameter(constructorAditionalParameter));
			}
		}

		//Parameter name resolving. Renaming arg0 to entityManager, etc.
		DomainType domain = getDomain();
		ParameterElement[] constructorAditionalParameters = null;
		
		if (domain != null && domain.getKind().isDeclared()) {
			TypeMirror domainType = ((DomainDeclaredType)domain).asType();
			if (domainType != null) {
				constructorAditionalParameters = parametersResolver.getConstructorAditionalParameters();
			}
		}
		
		for (ConverterParameter converterParameter: parameters) {
			if (/*asElement() == null && */constructorAditionalParameters != null) {
				for (ParameterElement additionalParameter: constructorAditionalParameters) {
					if (getTypeUtils().isSameType(additionalParameter.getType(), converterParameter.getType())) {
						converterParameter.setName(additionalParameter.getName());
						if (converterParameter.getName().equals("arg0")) {
							getMessager().printMessage(Kind.ERROR, "[2] Additional parameter is arg0 for " + this.getCanonicalName());
						}
					}
				}
			}
		}
		
		return parameters;
	}
	
	public DtoType getDto() {
		DomainType domain = getDomain();
		
		if (domain == null) {
			return null;
		}
		
		return domain.getDto();
	}
	
	public DomainType getDomain() {
		if (getConfiguration() == null) {
			return null;
		}
		
		return getConfiguration().getDomain();
	}

	/*
	public List<String> getLocalConverters() {
	
		List<String> result = new ArrayList<String>();

		if (!(getDomain() instanceof DomainDeclaredType)) {
			return result;
		}
		
		TypeElement domainElement = ((DomainDeclaredType)getDomain()).asConfigurationElement();

		if (domainElement == null) {
			return result;
		}
		
		for (int i = 0; i < domainElement.getTypeParameters().size(); i++) {
			result.add(TransferObjectContext.LOCAL_CONVERTER_NAME + i);
		}

		return result;
	}*/
}