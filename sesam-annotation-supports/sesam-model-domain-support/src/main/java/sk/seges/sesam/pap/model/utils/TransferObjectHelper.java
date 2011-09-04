package sk.seges.sesam.pap.model.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;

public class TransferObjectHelper {

	public static final String DTO_SUFFIX = "Dto";
	public static final String DEFAULT_SUFFIX = "Configuration";

	private static final String ID_METHOD_NAME = "id";

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

//	public Map<ExecutableElement, List<String>> getConverterParameterNames(TypeElement converterType, ParameterElement... additionalParameters) {
//		Map<ExecutableElement, List<String>> result = new HashMap<ExecutableElement, List<String>>();
//
//		List<ExecutableElement> constructors = ElementFilter.constructorsIn(converterType.getEnclosedElements());
//
//		for (ExecutableElement constructor : constructors) {
//
//			List<String> parameterNames = new LinkedList<String>();
//			result.put(constructor, parameterNames);
//
//			for (VariableElement parameter : constructor.getParameters()) {
//				parameterNames.add(parameter.getSimpleName().toString());
//			}
//
//			for (ParameterElement additionalParameter : additionalParameters) {
//				parameterNames.add(additionalParameter.getName().toString());
//			}
//		}
//
//		return result;
//	}
//
	public boolean isUnboxedType(TypeMirror typeMirror) {
		try {
			return (processingEnv.getTypeUtils().unboxedType(typeMirror) != null);
		} catch (IllegalArgumentException e) {
		}

		return false;
	}

	//Move to DtoTypeElement
	@Deprecated
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
				dto = new DomainTypeElement(type, processingEnv, roundEnv).getDtoTypeElement();
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

	//Move to DtoTypeElement
	@Deprecated
	public ImmutableType convertType(TypeMirror type) {
		return convertType(type, getNameTypes(), null);
	}

	//Move to DtoTypeElement
	@Deprecated
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
			ConfigurationTypeElement configurationElement = new DomainTypeElement(element.asType(), processingEnv, roundEnv).getConfigurationTypeElement();

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