package sk.seges.sesam.pap.model.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.model.converter.CollectionConfiguration;
import sk.seges.sesam.core.pap.NullCheck;
import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;

public class TransferObjectHelper {

	public static final String DTO_SUFFIX = "Dto";
	public static final String DEFAULT_SUFFIX = "Configuration";

	private static final String ID_METHOD_NAME = "id";

//	private static final Class<?>[] allowedClasses = new Class<?>[] { String.class, Date.class };

	private NameTypes nameTypes;
	private ProcessingEnvironment processingEnv;
	private RoundEnvironment roundEnv;
	private MethodHelper methodHelper;

	public TransferObjectHelper(NameTypes nameTypes, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, MethodHelper methodHelper) {
		this.nameTypes = nameTypes;
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
		this.methodHelper = methodHelper;
	}

	NameTypes getNameTypes() {
		return nameTypes;
	}

//	Class<?>[] getAllowedclasses() {
//		return allowedClasses;
//	}
//
//	private boolean isAllowedClass(NamedType namedType) {
//		for (Class<?> clazz : getAllowedclasses()) {
//			if (namedType.getCanonicalName().equals(clazz.getCanonicalName())) {
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//	public boolean isAllowedClass(TypeMirror type) {
//		for (Class<?> clazz : getAllowedclasses()) {
//			if (type.toString().equals(clazz.getCanonicalName())) {
//				return true;
//			}
//		}
//
//		return false;
//	}

	public Map<ExecutableElement, List<String>> getConverterParameterNames(TypeElement converterType, ParameterElement... additionalParameters) {
		Map<ExecutableElement, List<String>> result = new HashMap<ExecutableElement, List<String>>();

		List<ExecutableElement> constructors = ElementFilter.constructorsIn(converterType.getEnclosedElements());

		for (ExecutableElement constructor : constructors) {

			List<String> parameterNames = new LinkedList<String>();
			result.put(constructor, parameterNames);

			for (VariableElement parameter : constructor.getParameters()) {
				parameterNames.add(parameter.getSimpleName().toString());
			}

			for (ParameterElement additionalParameter : additionalParameters) {
				parameterNames.add(additionalParameter.getName().toString());
			}
		}

		return result;
	}

/*	public NamedType getDtoMappingClass(TypeMirror dtoType, TypeElement typeElement, ConfigurationType mappingType) {
		switch (dtoType.getKind()) {
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
		case VOID:
			if (mappingType.equals(ConfigurationType.DOMAIN) || mappingType.equals(ConfigurationType.DTO)) {
				return getNameTypes().toType(dtoType);
			}
			return null;
		case ERROR:
		case EXECUTABLE:
		case NONE:
		case NULL:
		case OTHER:
		case PACKAGE:
		case WILDCARD:
			processingEnv.getMessager().printMessage(Kind.ERROR, " [ERROR] Unsupported type kind - " + dtoType.getKind());
			return null;
		case DECLARED:
			DeclaredType declaredType = ((DeclaredType) dtoType);

			Element element = declaredType.asElement();

			if (mappingType.equals(ConfigurationType.CONVERTER) || mappingType.equals(ConfigurationType.CONFIGURATION)) {
				element = declaredType.asElement();
			}

			TypeElement result = mappingType.get(new TransferObjectConfiguration(element, processingEnv));
			if (result != null) {
				if (mappingType.equals(ConfigurationType.DOMAIN) || mappingType.equals(ConfigurationType.DTO)) {
					return convertTypeParameters(getNameTypes().toType(result.asType()), typeElement);
				}
				return getNameTypes().toType(result.asType());
			}
			result = mappingType.get(new TransferObjectConfiguration(typeElement, processingEnv), (TypeElement) element);
			if (result != null) {
				if (mappingType.equals(ConfigurationType.DOMAIN) || mappingType.equals(ConfigurationType.DTO)) {
					return convertTypeParameters(getNameTypes().toType(result.asType()), typeElement);
				}
				return getNameTypes().toType(result.asType());
			}
			if (mappingType.equals(ConfigurationType.DOMAIN) || mappingType.equals(ConfigurationType.DTO)) {
				return convertTypeParameters(getNameTypes().toType(dtoType), typeElement);
			}
			return null;
		case ARRAY:
			if (mappingType.equals(ConfigurationType.DOMAIN) || mappingType.equals(ConfigurationType.DTO)) {
				return new ArrayNamedType(getDtoMappingClass(((ArrayType) dtoType).getComponentType(), typeElement, mappingType));
			}
			return getDtoMappingClass(((ArrayType) dtoType).getComponentType(), typeElement, mappingType);
		case TYPEVAR:
			return getDtoMappingClass(ProcessorUtils.erasure(typeElement, ((TypeVariable) dtoType).asElement().getSimpleName().toString()),
					typeElement, mappingType);
		}

		if (mappingType.equals(ConfigurationType.DOMAIN) || mappingType.equals(ConfigurationType.DTO)) {
			return getNameTypes().toType(dtoType);
		}

		return null;
	}

	public NamedType convertTypeParameters(NamedType type, TypeElement typeElement) {
		if (type instanceof HasTypeParameters && ((HasTypeParameters) type).getTypeParameters() != null) {

			List<TypeParameter> domainParameters = new ArrayList<TypeParameter>();
			HasTypeParameters paramsType = ((HasTypeParameters) type);
			for (TypeParameter typeParameter : paramsType.getTypeParameters()) {
				if (typeParameter.getBounds() != null) {
					for (sk.seges.sesam.core.pap.model.api.TypeVariable bound : typeParameter.getBounds()) {

						if (bound.getUpperBound() instanceof NamedType) {
							NamedType namedType = (NamedType) bound.getUpperBound();
							TypeMirror boundType = namedType.asType();

							if (boundType != null) {
								domainParameters.add(TypeParameterBuilder.get(typeParameter.getVariable(),
										getDtoMappingClass(boundType, typeElement, ConfigurationType.DOMAIN)));
							} else {
								domainParameters.add(TypeParameterBuilder.get(
										typeParameter.getVariable(),
										getDtoMappingClass(
												processingEnv.getElementUtils()
														.getTypeElement(getNameTypes().toType(bound.getUpperBound()).getCanonicalName()).asType(),
												typeElement, ConfigurationType.DOMAIN)));
							}
						} else {
							domainParameters.add(TypeParameterBuilder.get(
									typeParameter.getVariable(),
									getDtoMappingClass(
											processingEnv.getElementUtils()
													.getTypeElement(getNameTypes().toType(bound.getUpperBound()).getCanonicalName()).asType(),
											typeElement, ConfigurationType.DOMAIN)));
						}
					}
				} else {
					domainParameters.add(TypeParameterBuilder.get(typeParameter.getVariable()));
				}
			}

			return TypedClassBuilder.get(type, domainParameters.toArray(new TypeParameter[] {}));
		}

		return type;
	}
*/
	private Class<?>[] getCommonConfigurations() {
		return new Class<?> [] {
				CollectionConfiguration.class
		};
	}

	public ConfigurationTypeElement getConfigurationForDto(TypeMirror dtoType) {

		if (dtoType.getKind().isPrimitive() || dtoType.getKind().equals(TypeKind.NONE)
				|| dtoType.getKind().equals(TypeKind.NULL) || dtoType.getKind().equals(TypeKind.ERROR)) {
			// cannot cast to domain
			return null;
		}

		Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(TransferObjectMapping.class);
		for (Element annotatedElement : elementsAnnotatedWith) {
			if (annotatedElement.asType().getKind().equals(TypeKind.DECLARED)) {
				ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement((TypeElement)annotatedElement, processingEnv, roundEnv);
	
				if (configurationTypeElement.appliesForDtoType(dtoType)) {
					return new ConfigurationTypeElement(null, (DeclaredType)dtoType, (TypeElement)annotatedElement, processingEnv, roundEnv);
				}
			}
		}

		if (getCommonConfigurations() != null) {
			for (Class<?> clazz: getCommonConfigurations()) {
				TypeElement configurationElement = processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
				if (configurationElement.getAnnotation(TransferObjectMapping.class) != null) {

					ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(configurationElement, processingEnv, roundEnv);

					if (configurationTypeElement.appliesForDtoType(dtoType)) {
						return new ConfigurationTypeElement(null, (DeclaredType)dtoType, configurationElement, processingEnv, roundEnv);
					}
				}
			}
		}
		return null;
	}

	public ConfigurationTypeElement getConfigurationForDomain(TypeMirror domainType) {

		if (domainType.getKind().isPrimitive() || domainType.getKind().equals(TypeKind.NONE)
				|| domainType.getKind().equals(TypeKind.NULL) || domainType.getKind().equals(TypeKind.ERROR)) {
			// cannot cast to DTO
			return null;
		}

		Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(TransferObjectMapping.class);
		for (Element annotatedElement : elementsAnnotatedWith) {
			if (annotatedElement.asType().getKind().equals(TypeKind.DECLARED)) {
				ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement((TypeElement)annotatedElement, processingEnv, roundEnv);
	
				if (configurationTypeElement.appliesForDomainType(domainType)) {
					return new ConfigurationTypeElement((DeclaredType)domainType, null, (TypeElement)annotatedElement, processingEnv, roundEnv);
				}
			}
		}

		if (getCommonConfigurations() != null) {
			for (Class<?> clazz: getCommonConfigurations()) {
				TypeElement configurationElement = processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
				if (configurationElement.getAnnotation(TransferObjectMapping.class) != null) {

					ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(configurationElement, processingEnv, roundEnv);

					if (configurationTypeElement.appliesForDomainType(domainType)) {
						return new ConfigurationTypeElement((DeclaredType)domainType, null, configurationElement, processingEnv, roundEnv);
					}
				}
			}
		}
		return null;
	}

	public DtoTypeElement toDto(TypeMirror domainType) {

		ConfigurationTypeElement transferObjectConfiguration = getConfigurationForDomain(domainType);

		if (transferObjectConfiguration != null) {
			return transferObjectConfiguration.getDtoTypeElement();
		}
		
		return null;
	}

	public boolean isUnboxedType(TypeMirror typeMirror) {
		try {
			return (processingEnv.getTypeUtils().unboxedType(typeMirror) != null);
		} catch (IllegalArgumentException e) {
		}

		return false;
	}

	public ImmutableType convertType(TypeMirror type, NameTypes nameTypes, ConfigurationTypeElement transferObjectConfiguration) {
		if (type == null || type.getKind().equals(TypeKind.NONE) || type.getKind().equals(TypeKind.NULL) || type.getKind().equals(TypeKind.ERROR)) {
			return null;
		}

		if (type.getKind().isPrimitive()) {
			return new InputClass(type, (String) null, type.toString().toLowerCase());
		}

		ImmutableType immutableType = nameTypes.toImmutableType(type);

		if (type.getKind().equals(TypeKind.DECLARED)) {

			DtoTypeElement dto = null;
			
			if (transferObjectConfiguration == null) {
				dto = toDto(type);
			}
			
			if (dto != null) {
				return dto;
			}

			DeclaredType declaredType = ((DeclaredType) type);

			if (declaredType.asElement().getKind().equals(ElementKind.ENUM)) {
				return immutableType;
			}

			if (isUnboxedType(declaredType)) {
				return immutableType;
			}

		} else if (type.getKind().equals(TypeKind.ARRAY)) {
			ArrayType arrayType = (ArrayType) type;
			NamedType componentType = convertType(arrayType.getComponentType());
			if (componentType != null) {
				return new ArrayNamedType(componentType);
			}
		}

		return immutableType;
	}
	
	public ImmutableType convertType(TypeMirror type) {
		return convertType(type, getNameTypes(), null);
	}

	public NamedType getDtoSuperclass(ConfigurationTypeElement configurationTypeElement) {

		DomainTypeElement superClassDomainType = configurationTypeElement.getDomainTypeElement().getSuperClass();

		if (superClassDomainType != null) {
			return convertType(superClassDomainType.asType());
		}

		return null;
	}

	public String getFieldPath(ExecutableElement method) {
		if (method == null) {
			return null;
		}

		Field fieldAnnotation = method.getAnnotation(Field.class);

		String fieldPath = methodHelper.toField(method);

		if (fieldAnnotation != null && NullCheck.checkNull(fieldAnnotation.value()) != null) {
			fieldPath = fieldAnnotation.value();
		}

		return fieldPath;
	}

	public ExecutableElement getDomainGetterMethod(Element element, String fieldName) {
		return getDomainMethod(element, new PathResolver(fieldName), MethodHelper.GETTER_PREFIX);
	}

	public ExecutableElement getDomainSetterMethod(Element element, String fieldName) {
		return getDomainMethod(element, new PathResolver(fieldName), MethodHelper.SETTER_PREFIX);
	}

	private ExecutableElement getDomainMethod(Element element, String fieldName, String prefix) {
		return getDomainMethod(element, new PathResolver(fieldName), prefix);
	}

	private ExecutableElement getDomainMethod(Element element, PathResolver pathResolver, String prefix) {

		if (!pathResolver.hasNext()) {
			return null;
		}

		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		String fieldName = pathResolver.next();

		for (ExecutableElement elementMethod : methods) {

			String currentPrefix = MethodHelper.GETTER_PREFIX;

			if (!pathResolver.hasNext()) {
				currentPrefix = prefix;
			}

			if (elementMethod.getModifiers().contains(Modifier.PUBLIC)
					&& elementMethod.getSimpleName().toString().equals(currentPrefix + methodHelper.toMethod(fieldName))) {
				if (!pathResolver.hasNext()) {
					return elementMethod;
				}

				if (elementMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
					return getDomainMethod(((DeclaredType) elementMethod.getReturnType()).asElement(), pathResolver.next(), prefix);
				}

				// incompatible types - nested path is expected, but declared
				// type was not found
				processingEnv.getMessager().printMessage(Kind.WARNING,
						"incompatible types - nested path (" + fieldName + ") is expected, but declared type was not found ", element);
				return null;
			}
		}

		if (element.getKind().equals(ElementKind.CLASS) || element.getKind().equals(ElementKind.INTERFACE)) {

			TypeElement typeElement = (TypeElement) element;
			if (typeElement.getSuperclass() != null && typeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
				pathResolver.reset();
				return getDomainMethod((TypeElement) ((DeclaredType) typeElement.getSuperclass()).asElement(), pathResolver, prefix);
			}
		}

		return null;
	}

	public ExecutableElement getDtoIdMethod(ConfigurationTypeElement configurationTypeElement) {

		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(configurationTypeElement.asElement().getEnclosedElements());

		TypeMirror domainType = configurationTypeElement.getDomainTypeElement().asType();

		if (!domainType.getKind().equals(TypeKind.DECLARED)) {
			return null;
		}
		
		for (ExecutableElement overridenMethod : overridenMethods) {

			Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
			if (ignoreAnnotation == null) {

				if (isIdMethod(overridenMethod)) {
					if (overridenMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
						return getDomainGetterMethod(((DeclaredType)domainType).asElement(), getFieldPath(overridenMethod));
					}

					return overridenMethod;
				}
			}
		}

		ExecutableElement idMethod = getIdMethod((DeclaredType)domainType);
		if (idMethod != null && !isFieldIgnored(configurationTypeElement, methodHelper.toField(idMethod))) {
			return idMethod;
		}

		return null;
	}

	public ExecutableElement getIdMethod(DeclaredType delcaredType) {

		Element element = delcaredType.asElement();
		
		if (element.getKind().equals(ElementKind.CLASS) || element.getKind().equals(ElementKind.INTERFACE)) {
			ConfigurationTypeElement configurationElement = getConfigurationForDomain(element.asType());

			if (configurationElement != null) {
				List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(configurationElement.asElement().getEnclosedElements());

				for (ExecutableElement overridenMethod : overridenMethods) {

					ExecutableElement domainMethod = getDomainGetterMethod(element, getFieldPath(overridenMethod));

					if (domainMethod != null && isIdMethod(domainMethod)) {
						return domainMethod;
					}
				}
			}
		}

		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		for (ExecutableElement method : methods) {
			if (isIdMethod(method)) {
				return method;
			}
		}

		if (element.getKind().equals(ElementKind.CLASS) || element.getKind().equals(ElementKind.INTERFACE)) {
			TypeElement typeElement = (TypeElement) element;
			ExecutableElement idMethod;

			if (typeElement.getSuperclass() != null && typeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
				idMethod = getIdMethod((DeclaredType)typeElement.getSuperclass());

				if (idMethod != null) {
					return idMethod;
				}
			}

			for (TypeMirror interfaceType : typeElement.getInterfaces()) {
				if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
					idMethod = getIdMethod((DeclaredType)interfaceType);
					if (idMethod != null) {
						return idMethod;
					}
				}
			}
		}

		return null;
	}

	public boolean isGetterMethod(ExecutableElement method) {
		return method.getSimpleName().toString().startsWith(MethodHelper.GETTER_PREFIX);
	}

	public boolean hasSetterMethod(TypeElement element, ExecutableElement method) {

		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		for (ExecutableElement elementMethod : methods) {

			if (elementMethod.getModifiers().contains(Modifier.PUBLIC)
					&& elementMethod.getSimpleName().toString().equals(methodHelper.toSetter(method)) && elementMethod.getParameters().size() == 1
					&& processingEnv.getTypeUtils().isAssignable(elementMethod.getParameters().get(0).asType(), method.getReturnType())) {
				return true;
			}
		}

		if (element.getSuperclass() != null && element.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			return hasSetterMethod((TypeElement) ((DeclaredType) element.getSuperclass()).asElement(), method);
		}

		return false;
	}

	private boolean isFieldIgnored(ConfigurationTypeElement configurationElement, String field) {

		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(configurationElement.asElement().getEnclosedElements());

		for (ExecutableElement overridenMethod : overridenMethods) {

			if (methodHelper.toField(overridenMethod).equals(field)) {
				Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
				if (ignoreAnnotation != null) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean isIdField(String field) {
		return field.equals(ID_METHOD_NAME);
	}

	public static boolean isIdMethod(ExecutableElement method) {
		if (method == null) {
			return false;
		}

		List<? extends AnnotationMirror> annotations = method.getAnnotationMirrors();

		for (AnnotationMirror annotation : annotations) {
			if (annotation.getAnnotationType().asElement().getSimpleName().toString().toLowerCase().equals(ID_METHOD_NAME)) {
				return true;
			}
		}

		String methodName = method.getSimpleName().toString().toLowerCase();
		return methodName.equals(MethodHelper.GETTER_PREFIX + ID_METHOD_NAME) || methodName.equals(ID_METHOD_NAME);
	}
}