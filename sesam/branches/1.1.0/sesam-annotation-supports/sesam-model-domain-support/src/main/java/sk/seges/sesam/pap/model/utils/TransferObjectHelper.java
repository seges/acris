package sk.seges.sesam.pap.model.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.NullCheck;
import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.model.mutable.MutableVariableElement;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.ImplementationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LayerType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.utils.TransferObjectConfiguration.DtoMappingType;

public class TransferObjectHelper {

	protected static final String DTO_SUFFIX = "Dto";
	private static final String ID_METHOD_NAME = "id";

	protected static final String DEFAULT_SUFFIX = "Configuration";

	private static final Class<?>[] allowedClasses = new Class<?>[] { String.class, Date.class };

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

	Class<?>[] getAllowedclasses() {
		return allowedClasses;
	}

	private boolean isAllowedClass(NamedType namedType) {
		for (Class<?> clazz : getAllowedclasses()) {
			if (namedType.getCanonicalName().equals(clazz.getCanonicalName())) {
				return true;
			}
		}

		return false;
	}

	public boolean isAllowedClass(TypeMirror type) {
		for (Class<?> clazz : getAllowedclasses()) {
			if (type.toString().equals(clazz.getCanonicalName())) {
				return true;
			}
		}

		return false;
	}

	public Map<ExecutableElement, List<String>> getConverterParameterNames(TypeElement converterType,
			MutableVariableElement... additionalParameters) {
		Map<ExecutableElement, List<String>> result = new HashMap<ExecutableElement, List<String>>();

		List<ExecutableElement> constructors = ElementFilter.constructorsIn(converterType.getEnclosedElements());

		for (ExecutableElement constructor : constructors) {

			List<String> parameterNames = new LinkedList<String>();
			result.put(constructor, parameterNames);

			for (VariableElement parameter : constructor.getParameters()) {
				parameterNames.add(parameter.getSimpleName().toString());
			}

			for (MutableVariableElement additionalParameter : additionalParameters) {
				parameterNames.add(additionalParameter.getSimpleName().toString());
			}
		}

		return result;
	}

	private TypeMirror getTypeParameter(DeclaredType type) {
		if (type.getTypeArguments() != null && type.getTypeArguments().size() == 1) {
			
			TypeMirror typeParameter = type.getTypeArguments().get(0);
			
			if (typeParameter.getKind().equals(TypeKind.DECLARED)) {
				return typeParameter;
			}
		}
		
		return null;
	}

	public TypeElement getDtoMappingClass(DeclaredType type) {
		if (ProcessorUtils.isCollection(type, processingEnv)) {
			TypeMirror typeParameter = getTypeParameter(type);
			if (typeParameter == null) {
				processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Type " + type.toString() +
						" should have defined a type parameter");
			} else {
				return (TypeElement)((DeclaredType)typeParameter).asElement();
			}
		} else if (ProcessorUtils.isPagedResult(type, processingEnv)) {
			TypeMirror typeParameter = getTypeParameter(type);
			if (typeParameter == null) {
				processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Type " + type.toString() +
						" should have defined a type parameter");
			} else {
				if (ProcessorUtils.isCollection(typeParameter, processingEnv)) {
					TypeMirror collectionTypeParameter = getTypeParameter((DeclaredType)typeParameter);
					if (collectionTypeParameter == null) {
						processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Type " + typeParameter +
								" should have defined a type parameter (originally used in the " + type.toString() + ")");
					} else {
						return (TypeElement)((DeclaredType)collectionTypeParameter).asElement();
					}
				} else {
					//TODO handle paged result that does not hold a collection of objects
				}
			}
		}
		
		return (TypeElement)type.asElement();
	}
	
	public NamedType getDtoMappingClass(TypeMirror dtoType, TypeElement typeElement, DtoMappingType mappingType) {
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
			if (mappingType.equals(DtoMappingType.DOMAIN) || mappingType.equals(DtoMappingType.DTO)) {
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
			processingEnv.getMessager().printMessage(Kind.ERROR,
					" [ERROR] Unsupported type kind - " + dtoType.getKind());
			return null;
		case DECLARED:
			DeclaredType declaredType = ((DeclaredType) dtoType);

			Element element = declaredType.asElement();

			if (mappingType.equals(DtoMappingType.CONVERTER) || mappingType.equals(DtoMappingType.CONFIGURATION)) {
				element = getDtoMappingClass(declaredType);
			}

			TypeElement result = mappingType.get(new TransferObjectConfiguration(element, processingEnv));
			if (result != null) {
				if (mappingType.equals(DtoMappingType.DOMAIN) || mappingType.equals(DtoMappingType.DTO)) {
					return convertTypeParameters(getNameTypes().toType(result.asType()), typeElement);
				}
				return getNameTypes().toType(result.asType());
			}
			result = mappingType
					.get(new TransferObjectConfiguration(typeElement, processingEnv), (TypeElement) element);
			if (result != null) {
				if (mappingType.equals(DtoMappingType.DOMAIN) || mappingType.equals(DtoMappingType.DTO)) {
					return convertTypeParameters(getNameTypes().toType(result.asType()), typeElement);
				}
				return getNameTypes().toType(result.asType());
			}
			if (mappingType.equals(DtoMappingType.DOMAIN) || mappingType.equals(DtoMappingType.DTO)) {
				return convertTypeParameters(getNameTypes().toType(dtoType), typeElement);
			}
			return null;
		case ARRAY:
			if (mappingType.equals(DtoMappingType.DOMAIN) || mappingType.equals(DtoMappingType.DTO)) {
				return new ArrayNamedType(getDtoMappingClass(((ArrayType) dtoType).getComponentType(), typeElement,
						mappingType));
			}
			return getDtoMappingClass(((ArrayType) dtoType).getComponentType(), typeElement, mappingType);
		case TYPEVAR:
			return getDtoMappingClass(ProcessorUtils.erasure(typeElement, ((TypeVariable) dtoType).asElement()
					.getSimpleName().toString()), typeElement, mappingType);
		}

		if (mappingType.equals(DtoMappingType.DOMAIN) || mappingType.equals(DtoMappingType.DTO)) {
			return getNameTypes().toType(dtoType);
		}

		return null;
	}

	public NamedType convertTypeParameters(NamedType type, TypeElement typeElement) {
		if (type instanceof HasTypeParameters) {

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
										getDtoMappingClass(boundType, typeElement, DtoMappingType.DOMAIN)));
							} else {
								domainParameters.add(TypeParameterBuilder.get(
										typeParameter.getVariable(),
										getDtoMappingClass(
												processingEnv
														.getElementUtils()
														.getTypeElement(
																getNameTypes().toType(bound.getUpperBound())
																		.getCanonicalName()).asType(), typeElement,
												DtoMappingType.DOMAIN)));
							}
						} else {
							domainParameters.add(TypeParameterBuilder.get(
									typeParameter.getVariable(),
									getDtoMappingClass(
											processingEnv
													.getElementUtils()
													.getTypeElement(
															getNameTypes().toType(bound.getUpperBound())
																	.getCanonicalName()).asType(), typeElement,
											DtoMappingType.DOMAIN)));
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

	public Element getConfigurationElement(TypeElement element, RoundEnvironment roundEnv) {

		if (element.asType().getKind().isPrimitive() || element.asType().getKind().equals(TypeKind.NONE)
				|| element.asType().getKind().equals(TypeKind.NULL)
				|| element.asType().getKind().equals(TypeKind.ERROR)) {
			// cannot cast to DTO
			return null;
		}

		Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(TransferObjectMapping.class);
		for (Element annotatedElement : elementsAnnotatedWith) {
			TypeElement domainObjectClass = getDomainTypeElement(annotatedElement);

			// Dto is going to be generated
			if (domainObjectClass != null && domainObjectClass.equals(element)) {
				return annotatedElement;
			}
		}

		return null;
	}

	public static PackageValidatorProvider getPackageValidationProvider() {
		return new DefaultPackageValidatorProvider();
	}

	public static ImmutableType getDtoType(ImmutableType configurationType) {
		PackageValidator packageValidator = getPackageValidationProvider().get(configurationType)
				.moveTo(LocationType.SHARED).moveTo(LayerType.MODEL).clearType().moveTo(ImplementationType.DTO);
		configurationType = configurationType.changePackage(packageValidator);
		return configurationType.getSimpleName().endsWith(DEFAULT_SUFFIX) ? configurationType.setName(configurationType
				.getSimpleName().substring(0, configurationType.getSimpleName().length() - DEFAULT_SUFFIX.length()))
				: configurationType.addClassSufix(DTO_SUFFIX);
	}

	public ImmutableType toDto(TypeElement element, RoundEnvironment roundEnv) {

		Element configurationElement = getConfigurationElement(element, roundEnv);

		if (configurationElement != null) {

			TransferObjectConfiguration transferObjectConfiguration = new TransferObjectConfiguration(
					configurationElement, processingEnv);

			TypeElement dto = transferObjectConfiguration.getDto();

			if (dto != null) {
				return getNameTypes().toImmutableType(dto);
			}

			if (transferObjectConfiguration.getConfiguration() != null) {
				configurationElement = transferObjectConfiguration.getConfiguration();

				transferObjectConfiguration = new TransferObjectConfiguration(configurationElement, processingEnv);

				dto = transferObjectConfiguration.getDto();

				if (dto != null) {
					return getNameTypes().toImmutableType(dto);
				}
			}

			return getDtoType((ImmutableType) getNameTypes().toType(configurationElement));
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

	public NamedType convertType(TypeMirror type) {
		if (type == null || type.getKind().equals(TypeKind.NONE) || type.getKind().equals(TypeKind.NULL)
				|| type.getKind().equals(TypeKind.ERROR)) {
			return null;
		}

		if (type.getKind().isPrimitive()) {
			return new InputClass(type, (String) null, type.toString().toLowerCase());
		}

		NamedType namedType = getNameTypes().toType(type);

		if (type.getKind().equals(TypeKind.DECLARED)) {

			DeclaredType declaredType = ((DeclaredType) type);

			if (declaredType.getTypeArguments() != null && declaredType.getTypeArguments().size() > 0) {

				//TODO handle paged result
				if (ProcessorUtils.implementsType(declaredType.asElement().asType(), processingEnv.getElementUtils()
						.getTypeElement(Collection.class.getCanonicalName()).asType())) {

					TypeMirror arg = declaredType.getTypeArguments().get(0);

					if (arg.getKind().equals(TypeKind.DECLARED)) {
						NamedType dto = convertType(arg);
						if (dto != null) {
							return TypedClassBuilder.get(namedType, dto);
						}
					}

					return namedType;

				} else if (ProcessorUtils.implementsType(declaredType.asElement().asType(), processingEnv
						.getElementUtils().getTypeElement(Map.class.getCanonicalName()).asType())
						&& declaredType.getTypeArguments().size() == 2) {

					TypeMirror key = declaredType.getTypeArguments().get(0);
					TypeMirror value = declaredType.getTypeArguments().get(1);

					NamedType keyType = null;

					if (key.getKind().equals(TypeKind.DECLARED)) {
						NamedType dto = convertType(key);
						if (dto != null) {
							keyType = dto;
						}
					}

					if (keyType == null) {
						keyType = getNameTypes().toType(key);
					}

					NamedType valueType = null;

					if (value.getKind().equals(TypeKind.DECLARED)) {
						NamedType dto = convertType(value);
						if (dto != null) {
							valueType = dto;
						}
					}

					if (valueType == null) {
						valueType = getNameTypes().toType(value);
					}

					return TypedClassBuilder.get(namedType, keyType, valueType);
				}
			}

			if (isAllowedClass(namedType)) {
				return namedType;
			}

			ImmutableType dto = toDto((TypeElement) declaredType.asElement(), roundEnv);
			if (dto != null) {
				return dto;
			}

			if (declaredType.asElement().getKind().equals(ElementKind.ENUM)) {
				return namedType;
			}

			if (isUnboxedType(declaredType)) {
				return namedType;
			}

		} else if (type.getKind().equals(TypeKind.ARRAY)) {
			ArrayType arrayType = (ArrayType) type;
			NamedType componentType = convertType(arrayType.getComponentType());
			if (componentType != null) {
				return new ArrayNamedType(componentType);
			}
		}

		return null;
	}

	public NamedType getDtoSuperclass(TypeElement typeElement) {

		TypeElement domainObjectClass = getDomainTypeElement(typeElement);

		if (domainObjectClass != null) {
			return convertType(domainObjectClass.getSuperclass());
		}

		return null;
	}

	public boolean isDelegateConfiguration(Element configurationElement) {
		if (configurationElement == null) {
			return false;
		}
		return new TransferObjectConfiguration(configurationElement, processingEnv).getConfiguration() != null;
	}

	public TypeElement getDomainTypeElement(Element configurationElement) {
		return new TransferObjectConfiguration(configurationElement, processingEnv).getDomain();
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
					return getDomainMethod(((DeclaredType) elementMethod.getReturnType()).asElement(),
							pathResolver.next(), prefix);
				}

				// incompatible types - nested path is expected, but declared
				// type was not found
				processingEnv.getMessager().printMessage(
						Kind.WARNING,
						"incompatible types - nested path (" + fieldName
								+ ") is expected, but declared type was not found ", element);
				return null;
			}
		}

		if (element.getKind().equals(ElementKind.CLASS) || element.getKind().equals(ElementKind.INTERFACE)) {

			TypeElement typeElement = (TypeElement) element;
			if (typeElement.getSuperclass() != null && typeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
				pathResolver.reset();
				return getDomainMethod((TypeElement) ((DeclaredType) typeElement.getSuperclass()).asElement(),
						pathResolver, prefix);
			}
		}

		return null;
	}

	public ExecutableElement getDtoIdMethod(TypeElement configurationElement) {

		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(configurationElement.getEnclosedElements());

		TypeElement domainObjectClass = getDomainTypeElement(configurationElement);

		if (domainObjectClass == null) {
			return null;
		}

		for (ExecutableElement overridenMethod : overridenMethods) {

			Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
			if (ignoreAnnotation == null) {

				if (isIdMethod(overridenMethod)) {
					if (overridenMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
						return getDomainGetterMethod(domainObjectClass, getFieldPath(overridenMethod));
					}

					return overridenMethod;
				}
			}
		}

		ExecutableElement idMethod = getIdMethod(domainObjectClass);
		if (idMethod != null && !isFieldIgnored(configurationElement, methodHelper.toField(idMethod))) {
			return idMethod;
		}

		return null;
	}

	public ExecutableElement getIdMethod(Element element) {
		
		if (element.getKind().equals(ElementKind.CLASS) || element.getKind().equals(ElementKind.INTERFACE)) {
			Element configurationElement = getConfigurationElement((TypeElement)element, roundEnv);
			
			if (configurationElement != null) {
				List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(configurationElement.getEnclosedElements());
				
				for (ExecutableElement overridenMethod: overridenMethods) {
	
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
				idMethod = getIdMethod(((DeclaredType) typeElement.getSuperclass()).asElement());

				if (idMethod != null) {
					return idMethod;
				}
			}

			for (TypeMirror interfaceType : typeElement.getInterfaces()) {
				if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
					idMethod = getIdMethod(((DeclaredType) interfaceType).asElement());
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
					&& elementMethod.getSimpleName().toString().equals(methodHelper.toSetter(method))
					&& elementMethod.getParameters().size() == 1
					&& processingEnv.getTypeUtils().isAssignable(elementMethod.getParameters().get(0).asType(),
							method.getReturnType())) {
				return true;
			}
		}

		if (element.getSuperclass() != null && element.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			return hasSetterMethod((TypeElement) ((DeclaredType) element.getSuperclass()).asElement(), method);
		}

		return false;
	}

	private boolean isFieldIgnored(TypeElement configurationElement, String field) {

		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(configurationElement.getEnclosedElements());

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

	public boolean isIdMethod(ExecutableElement method) {
		if (method == null) {
			return false;
		}

		List<? extends AnnotationMirror> annotations = method.getAnnotationMirrors();

		for (AnnotationMirror annotation : annotations) {
			if (annotation.getAnnotationType().asElement().getSimpleName().toString().toLowerCase()
					.equals(ID_METHOD_NAME)) {
				return true;
			}
		}

		String methodName = method.getSimpleName().toString().toLowerCase();
		return methodName.equals(MethodHelper.GETTER_PREFIX + ID_METHOD_NAME) || methodName.equals(ID_METHOD_NAME);
	}
}