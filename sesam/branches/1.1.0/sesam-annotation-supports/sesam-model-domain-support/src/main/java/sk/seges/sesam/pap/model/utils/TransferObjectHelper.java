package sk.seges.sesam.pap.model.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.PathResolver;
import sk.seges.sesam.pap.model.utils.TransferObjectConfiguration.DtoMappingType;

public class TransferObjectHelper {

	protected static final String DTO_SUFFIX = "Dto";
	protected static final String SETTER_PREFIX = "set";
	protected static final String GETTER_PREFIX = "get";
	private static final String ID_METHOD_NAME = "id";

	protected static final String DEFAULT_SUFFIX = "Configuration";

	private static final Class<?>[] allowedClasses = new Class<?>[] {
		String.class, Date.class
	};

	private NameTypes nameTypes;
	private ProcessingEnvironment processingEnv;
	private RoundEnvironment roundEnv;
	
	public TransferObjectHelper(NameTypes nameTypes, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		this.nameTypes = nameTypes;
		this.processingEnv = processingEnv;
		this.roundEnv = roundEnv;
	}
	
	NameTypes getNameTypes() {
		return nameTypes;
	}
	
	Class<?>[] getAllowedclasses() {
		return allowedClasses;
	}
	
	private boolean isAllowedClass(NamedType namedType) {
		for (Class<?> clazz: getAllowedclasses()) {
			if (namedType.getCanonicalName().equals(clazz.getCanonicalName())) {
				return true;
			}
		}  	
		
		return false;
	}

	public boolean isAllowedClass(TypeMirror type) {
		for (Class<?> clazz: getAllowedclasses()) {
			if (type.toString().equals(clazz.getCanonicalName())) {
				return true;
			}
		}  	
		
		return false;
	}

	public String toMethod(String name) {
		
		if (name.length() < 2) {
			return name.toUpperCase();
		}
		
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public String toMethod(String prefix, String fieldName) {
		PathResolver pathResolver = new PathResolver(fieldName);

		if (pathResolver.isNested()) {
			
			int i = 0;

			String result = "";

			while (pathResolver.hasNext()) {
				if (i > 0) {
					result += ".";
				}
				String path = pathResolver.next();
				
				if (pathResolver.hasNext()) {
					result += toGetter(path);
				} else {
					result += toMethod(prefix, path);
				}
				i++;
			}
			
			return result;
		}
		
		return prefix + toMethod(fieldName);
	}
	
	
	private String toSetter(ExecutableElement method) {
		if (method.getSimpleName().toString().startsWith(GETTER_PREFIX)) {
			return toSetter(method.getSimpleName().toString().substring(GETTER_PREFIX.length()));
		}
		return toSetter(method.getSimpleName().toString());
	}

	public String toField(String fieldName) {
		String[] pathParts = fieldName.split("\\.");
		String result = "";
		
		for (String path: pathParts) {
			result += toMethod(path);
		}
		
		if (result.length() < 2) {
			return result.toLowerCase();
		}
		
		return result.substring(0, 1).toLowerCase() + result.substring(1);
	}
	
	public String toField(ExecutableElement getterMethod) {
		
		String result = "";
		
		if (getterMethod.getSimpleName().toString().startsWith(GETTER_PREFIX)) {
			result = getterMethod.getSimpleName().toString().substring(GETTER_PREFIX.length());		
		} else {
			result = getterMethod.getSimpleName().toString();
		}
		
		if (result.length() < 2) {
			return result.toLowerCase();
		}
		
		return result.substring(0, 1).toLowerCase() + result.substring(1);
	}

	public String toGetter(String fieldName) {
		return toMethod(GETTER_PREFIX, fieldName) + "()";
	}
	
	public String toGetter(ExecutableElement method) {
		return toGetter(method.getSimpleName().toString());
	}

	public String toSetter(String fieldName) {
		return toMethod(SETTER_PREFIX, fieldName);
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
			processingEnv.getMessager().printMessage(Kind.ERROR, " [ERROR] Unsupported type kind - " + dtoType.getKind());
			return null;
		case DECLARED:
			DeclaredType declaredType = ((DeclaredType)dtoType);

			if (mappingType.equals(DtoMappingType.CONVERTER) || mappingType.equals(DtoMappingType.CONFIGURATION)) {
				if (ProcessorUtils.implementsType(declaredType.asElement().asType(), processingEnv.getElementUtils().getTypeElement(Collection.class.getCanonicalName()).asType())) {
					
					TypeMirror parameterType = declaredType.getTypeArguments().get(0);
					return getDtoMappingClass(parameterType, typeElement, mappingType);
				}
			}
			
			Element element = declaredType.asElement();
			TypeElement result = mappingType.get(new TransferObjectConfiguration(element, processingEnv));
			if (result != null) {
				if (mappingType.equals(DtoMappingType.DOMAIN) || mappingType.equals(DtoMappingType.DTO)) {
					return convertTypeParameters(getNameTypes().toType(result.asType()), typeElement);
				}
				return getNameTypes().toType(result.asType());
			}
			result = mappingType.get(new TransferObjectConfiguration(typeElement, processingEnv), (TypeElement)element);
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
				return new ArrayNamedType(getDtoMappingClass(((ArrayType)dtoType).getComponentType(), typeElement, mappingType));
			}
			return getDtoMappingClass(((ArrayType)dtoType).getComponentType(), typeElement, mappingType);
		case TYPEVAR:
			return getDtoMappingClass(ProcessorUtils.erasure(typeElement, ((TypeVariable)dtoType).asElement().getSimpleName().toString()), typeElement, mappingType);
		}

		if (mappingType.equals(DtoMappingType.DOMAIN) || mappingType.equals(DtoMappingType.DTO)) {
			return getNameTypes().toType(dtoType);
		}
		
		return null;
	}

	public NamedType convertTypeParameters(NamedType type, TypeElement typeElement) {
		if (type instanceof HasTypeParameters) {
			
			List<TypeParameter> domainParameters = new ArrayList<TypeParameter>();
			HasTypeParameters paramsType = ((HasTypeParameters)type);
			
			for (TypeParameter typeParameter: paramsType.getTypeParameters()) {
				if (typeParameter.getBounds() != null) {
					for (sk.seges.sesam.core.pap.model.api.TypeVariable bound: typeParameter.getBounds()) {
						
						if (bound.getUpperBound() instanceof NamedType) {
							NamedType namedType = (NamedType)bound.getUpperBound();
							TypeMirror boundType = namedType.asType();
							
							if (boundType != null) {
								domainParameters.add(
										TypeParameterBuilder.get(typeParameter.getVariable(), 
												getDtoMappingClass(boundType, typeElement, DtoMappingType.DOMAIN)));
							} else {
								domainParameters.add(
										TypeParameterBuilder.get(typeParameter.getVariable(), 
											getDtoMappingClass(processingEnv.getElementUtils().getTypeElement(
													getNameTypes().toType(bound.getUpperBound()).getCanonicalName()).asType(), typeElement, DtoMappingType.DOMAIN)));
							}
						} else {
							domainParameters.add(
									TypeParameterBuilder.get(typeParameter.getVariable(), 
										getDtoMappingClass(processingEnv.getElementUtils().getTypeElement(
												getNameTypes().toType(bound.getUpperBound()).getCanonicalName()).asType(), typeElement, DtoMappingType.DOMAIN)));
						}
					}
				}
			}
			
			return TypedClassBuilder.get(type, domainParameters.toArray(new TypeParameter[] {}));
		}
		
		return type;
	}
	
	public Element getConfigurationElement(TypeElement element, RoundEnvironment roundEnv) {
		
		if (element.asType().getKind().isPrimitive() || element.asType().getKind().equals(TypeKind.NONE) || element.asType().getKind().equals(TypeKind.NULL) || element.asType().getKind().equals(TypeKind.ERROR)) {
			//cannot cast to DTO
			return null;
		}
		
		Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(TransferObjectMapping.class);
		for (Element annotatedElement: elementsAnnotatedWith) {
			TypeElement domainObjectClass = getDomainTypeElement(annotatedElement);

			//Dto is going to be generated
			if (domainObjectClass != null && domainObjectClass.equals(element)) {
				return annotatedElement;
			}
		}
		
		return null;
	}

	public static ImmutableType getDtoType(ImmutableType configurationType) {
		return configurationType.getSimpleName().endsWith(DEFAULT_SUFFIX) ?
				configurationType.setName(configurationType.getSimpleName().substring(0, configurationType.getSimpleName().length() - DEFAULT_SUFFIX.length())) :
				configurationType.addClassSufix(DTO_SUFFIX);
	}

	public ImmutableType toDto(TypeElement element, RoundEnvironment roundEnv) {
		
		Element configurationElement = getConfigurationElement(element, roundEnv);
				
		if (configurationElement != null) {
			
			TypeElement dto = new TransferObjectConfiguration(configurationElement, processingEnv).getDto();
			
			if (dto != null) {
				return getNameTypes().toImmutableType(dto);
			}

			return getDtoType((ImmutableType)getNameTypes().toType(configurationElement));
		}
		
		return null;
	}

	public boolean isUnboxedType(TypeMirror typeMirror) {
		try {
			return (processingEnv.getTypeUtils().unboxedType(typeMirror) != null);
		} catch (IllegalArgumentException e) {}
		
		return false;
	}

	public NamedType convertType(TypeMirror type) {
		if (type == null || type.getKind().equals(TypeKind.NONE) || type.getKind().equals(TypeKind.NULL) || type.getKind().equals(TypeKind.ERROR)) {
			return null;
		}
		
		if (type.getKind().isPrimitive()) {
			return new InputClass((String)null, type.toString().toLowerCase());
		}
		
		NamedType namedType = getNameTypes().toType(type);
		
		if (type.getKind().equals(TypeKind.DECLARED)) {

			DeclaredType declaredType = ((DeclaredType)type);

			if (declaredType.getTypeArguments() != null && declaredType.getTypeArguments().size() > 0) {

				if (ProcessorUtils.implementsType(declaredType.asElement().asType(), processingEnv.getElementUtils().getTypeElement(Collection.class.getCanonicalName()).asType())) {
	
					TypeMirror arg = declaredType.getTypeArguments().get(0);
	
					if (arg.getKind().equals(TypeKind.DECLARED)) {
						NamedType dto = convertType(arg);
						if (dto != null) {
							return TypedClassBuilder.get(namedType, dto);
						}
					}
	
					return namedType;
					
				} else if (ProcessorUtils.implementsType(declaredType.asElement().asType(), 
						processingEnv.getElementUtils().getTypeElement(Map.class.getCanonicalName()).asType()) && declaredType.getTypeArguments().size() == 2 ) {
					
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
			
			ImmutableType dto = toDto((TypeElement)declaredType.asElement(), roundEnv);
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
			ArrayType arrayType = (ArrayType)type;
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

	public TypeElement getDomainTypeElement(Element configurationElement) {
		return new TransferObjectConfiguration(configurationElement, processingEnv).getDomain();
	}

	public String getFieldPath(ExecutableElement method) {
		if (method == null) {
			return null;
		}
		
		Field fieldAnnotation = method.getAnnotation(Field.class);

		String fieldPath = toField(method);

		if (fieldAnnotation != null && NullCheck.checkNull(fieldAnnotation.value()) != null) {
			fieldPath = fieldAnnotation.value();
		}
		
		return fieldPath;
	}

	public ExecutableElement getDomainGetterMethod(Element element, String fieldName) {
		return getDomainMethod(element, new PathResolver(fieldName), GETTER_PREFIX);
	}

	public ExecutableElement getDomainSetterMethod(Element element, String fieldName) {
		return getDomainMethod(element, new PathResolver(fieldName), SETTER_PREFIX);
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
		
		for (ExecutableElement elementMethod: methods) {
			
			String currentPrefix = GETTER_PREFIX;
			
			if (!pathResolver.hasNext()) {
				currentPrefix = prefix;
			}
			
			if (elementMethod.getModifiers().contains(Modifier.PUBLIC) && 
				elementMethod.getSimpleName().toString().equals(currentPrefix + toMethod(fieldName))) {
				if (!pathResolver.hasNext()) {
					return elementMethod;
				}
				
				if (elementMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
					return getDomainMethod(((DeclaredType)elementMethod.getReturnType()).asElement(), pathResolver.next(), prefix);
				}
				
				//incompatible types - nested path is expected, but declared type was not found
				processingEnv.getMessager().printMessage(Kind.WARNING, "incompatible types - nested path (" + fieldName + ") is expected, but declared type was not found ", element);
				return null;
			}
		}
		
		if (element.getKind().equals(ElementKind.CLASS) || element.getKind().equals(ElementKind.INTERFACE)) {
		
			TypeElement typeElement = (TypeElement)element;
			if (typeElement.getSuperclass() != null && typeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
				return getDomainMethod((TypeElement)((DeclaredType)typeElement.getSuperclass()).asElement(), fieldName, prefix);
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
		
		for (ExecutableElement overridenMethod: overridenMethods) {

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
		if (idMethod != null && !isFieldIgnored(configurationElement, toField(idMethod))) {
			return idMethod;
		}

		return null;
	}
	
	public ExecutableElement getIdMethod(Element element) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());
		
		for (ExecutableElement method: methods) {
			if (isIdMethod(method)) {
				return method;
			}
		}
		
		if (element.getKind().equals(ElementKind.CLASS) || element.getKind().equals(ElementKind.INTERFACE)) {
			TypeElement typeElement = (TypeElement)element;
			ExecutableElement idMethod;
			
			if (typeElement.getSuperclass() != null && typeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
				idMethod = getIdMethod(((DeclaredType)typeElement.getSuperclass()).asElement());
				
				if (idMethod != null) {
					return idMethod;
				}
			}

			for (TypeMirror interfaceType: typeElement.getInterfaces()) {
				if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
					idMethod = getIdMethod(((DeclaredType)interfaceType).asElement());
					if (idMethod != null) {
						return idMethod;
					}
				}
			}
		}
		
		return null;
	}

	public boolean isGetterMethod(ExecutableElement method) {
		return method.getSimpleName().toString().startsWith(GETTER_PREFIX);
	}
	
	public boolean hasSetterMethod(TypeElement element, ExecutableElement method) {

		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		for (ExecutableElement elementMethod: methods) {
			
			if (elementMethod.getModifiers().contains(Modifier.PUBLIC) && 
				elementMethod.getSimpleName().toString().equals(toSetter(method)) &&
				elementMethod.getParameters().size() == 1 &&
				processingEnv.getTypeUtils().isAssignable(elementMethod.getParameters().get(0).asType(),
						method.getReturnType())) {
				return true;
			}
		}
		
		if (element.getSuperclass() != null && element.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			return hasSetterMethod((TypeElement)((DeclaredType)element.getSuperclass()).asElement(), method);
		}
		
		return false;
	}
	
	private boolean isFieldIgnored(TypeElement configurationElement, String field) {
		
		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(configurationElement.getEnclosedElements());
		
		for (ExecutableElement overridenMethod: overridenMethods) {

			if (toField(overridenMethod).equals(field)) {
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
		
		for (AnnotationMirror annotation: annotations) {
			if (annotation.getAnnotationType().asElement().getSimpleName().toString().toLowerCase().equals(ID_METHOD_NAME)) {
				return true;
			}
		}
		
		String methodName = method.getSimpleName().toString().toLowerCase();
		return methodName.equals(GETTER_PREFIX + ID_METHOD_NAME) || methodName.equals(ID_METHOD_NAME);
	}
}