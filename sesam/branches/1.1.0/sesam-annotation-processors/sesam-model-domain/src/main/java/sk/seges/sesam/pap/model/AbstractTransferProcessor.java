package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.NullCheck;
import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Id;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectConfiguration;

public abstract class AbstractTransferProcessor extends AbstractConfigurableProcessor {

	private static final String ID_METHOD_NAME = "id";

	protected class ProcessorContext {
		private TypeElement configurationElement;
		private Modifier modifier;
		private ExecutableElement method;
		private ExecutableElement domainMethod;
		
		private TypeMirror domainMethodReturnType;

		private NamedType fieldType;
		private String fieldName;

		private TypeElement domainTypeElement;
		private String domainFieldName;
		private String domainFieldPath;
		
		public ProcessorContext(TypeElement typeElement, Modifier modifier, ExecutableElement method) {
			this(typeElement, modifier, method, method);
		}
		
		public void setDomainMethodReturnType(TypeMirror domainMethodReturnType) {
			this.domainMethodReturnType = domainMethodReturnType;
		}
		
		public TypeMirror getDomainMethodReturnType() {
			return domainMethodReturnType;
		}
		
		public TypeElement getDomainTypeElement() {
			return domainTypeElement;
		}
		
		public void setDomainTypeElement(TypeElement domainTypeElement) {
			this.domainTypeElement = domainTypeElement;
		}
		
		public String getDomainFieldPath() {
			return domainFieldPath;
		}
		
		public void setDomainFieldPath(String domainFieldPath) {
			this.domainFieldPath = domainFieldPath;
		}
		
		public void setDomainFieldName(String domainFieldName) {
			this.domainFieldName = domainFieldName;
		}
		
		public String getDomainFieldName() {
			return domainFieldName;
		}
		
		public void setFieldType(NamedType fieldType) {
			this.fieldType = fieldType;
		}
		
		public NamedType getFieldType() {
			return fieldType;
		}
		
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		
		public ProcessorContext(TypeElement configurationElement, Modifier modifier, ExecutableElement method, ExecutableElement domainMethod) {
			setConfigurationElement(configurationElement);
			setModifier(modifier);
			setMethod(method);
			setDomainMethod(domainMethod);
		}
		
		public void setConfigurationElement(TypeElement configurationElement) {
			this.configurationElement = configurationElement;
		}
		
		public TypeElement getConfigurationElement() {
			return configurationElement;
		}
		
		public void setModifier(Modifier modifier) {
			this.modifier = modifier;
		}
		
		public Modifier getModifier() {
			return modifier;
		}

		public void setMethod(ExecutableElement method) {
			this.method = method;
		}
		
		public ExecutableElement getMethod() {
			return method;
		}
		
		public ExecutableElement getDomainMethod() {
			return domainMethod;
		}
		
		public void setDomainMethod(ExecutableElement domainMethod) {
			this.domainMethod = domainMethod;
		}
	}
	
	protected interface ElementPrinter {
		
		void initialize(TypeElement typeElement);
		
		void print(ProcessorContext context);
		
		void finish(TypeElement typeElement);
	}

	protected abstract class AbstractElementPrinter implements ElementPrinter {
		
		protected PrintWriter pw;
		
		public AbstractElementPrinter(PrintWriter pw) {
			this.pw = pw;
		}
		
		@Override
		public void initialize(TypeElement typeElement) {
		}
		
		@Override
		public void finish(TypeElement typeElement) {
		}
	}
	
	protected static final String DEFAULT_SUFFIX = "Configuration";
	protected static final String DTO_SUFFIX = "Dto";
	protected static final String SETTER_PREFIX = "set";
	protected static final String GETTER_PREFIX = "get";

	private static final Class<?>[] allowedClasses = new Class<?>[] {
		String.class, Date.class
	};

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(TransferObjectConfiguration.class.getCanonicalName());
		return annotations;
	}
	
	protected NamedType getDtoSuperclass(TypeElement typeElement) {

		TypeElement domainObjectClass = getDomainTypeElement(typeElement);

		if (domainObjectClass != null) {
			return convertType(domainObjectClass.getSuperclass()/*, false*/);
		}		
		
		return null;
	}

	protected TypeElement getDomainTypeElement(Element configurationElement) {
		TransferObjectConfiguration transferObjectConfiguration =  configurationElement.getAnnotation(TransferObjectConfiguration.class);
		TypeElement domainObjectClass = AnnotationClassPropertyHarvester.getTypeOfClassProperty(transferObjectConfiguration, new AnnotationClassProperty<TransferObjectConfiguration>() {

			@Override
			public Class<?> getClassProperty(TransferObjectConfiguration annotation) {
				return annotation.value();
			}
		});

		return domainObjectClass;
	}

	public static Class<?>[] getAllowedclasses() {
		return allowedClasses;
	}
	
	protected boolean isAllowedClass(NamedType namedType) {
		for (Class<?> clazz: getAllowedclasses()) {
			if (namedType.getCanonicalName().equals(clazz.getCanonicalName())) {
				return true;
			}
		}  	
		
		return false;
	}

	protected boolean isAllowedClass(TypeMirror type) {
		for (Class<?> clazz: getAllowedclasses()) {
			if (type.toString().equals(clazz.getCanonicalName())) {
				return true;
			}
		}  	
		
		return false;
	}

	protected TypeMirror getTargetEntityType(ExecutableElement method) {
		return method.getReturnType();
	}

	protected boolean isUnboxedType(TypeMirror mirronr) {
		try {
			return (processingEnv.getTypeUtils().unboxedType(mirronr) != null);
		} catch (IllegalArgumentException e) {}
		
		return false;
	}
	
	protected NamedType convertType(TypeMirror type) {
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

				if (implementsType(declaredType.asElement().asType(), processingEnv.getElementUtils().getTypeElement(Collection.class.getCanonicalName()).asType())) {
	
					TypeMirror arg = declaredType.getTypeArguments().get(0);
	
					if (arg.getKind().equals(TypeKind.DECLARED)) {
						NamedType dto = convertType(arg);
						if (dto != null) {
							return TypedClassBuilder.get(namedType, dto);
						}
					}
	
					return namedType;
					
				} else if (implementsType(declaredType.asElement().asType(), 
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
			
			MutableType dto = toDto((TypeElement)declaredType.asElement(), roundEnv/*, deepSearch*/);
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
	
	protected boolean implementsType(TypeMirror t1, TypeMirror t2) {
		if (t1 == null || !t1.getKind().equals(TypeKind.DECLARED) || !t2.getKind().equals(TypeKind.DECLARED)) {
			return false;
		}
		
		DeclaredType dt1 = (DeclaredType)t1;
		DeclaredType dt2 = (DeclaredType)t2;

		for (TypeMirror interfaceType: ((TypeElement)dt1.asElement()).getInterfaces()) {
			
			if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
				if (((DeclaredType)interfaceType).asElement().equals(dt2.asElement())) {
					return true;
				}
				
				if (implementsType(interfaceType, t2)) {
					return true;
				}
			}
			
		}

		TypeMirror superClassType = ((TypeElement)dt1.asElement()).getSuperclass();
		
		if (superClassType.getKind().equals(TypeKind.DECLARED)) {
			if (((DeclaredType)superClassType).asElement().equals(dt2.asElement())) {
				return true;
			}		
		}

		return implementsType(superClassType, t2);
	}

	protected Element getConfigurationElement(TypeElement element, RoundEnvironment roundEnv/*, boolean deepSearch*/) {
		
		if (element.asType().getKind().isPrimitive() || element.asType().getKind().equals(TypeKind.NONE) || element.asType().getKind().equals(TypeKind.NULL) || element.asType().getKind().equals(TypeKind.ERROR)) {
			//cannot cast to DTO
			return null;
		}
		
		Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(TransferObjectConfiguration.class);
		for (Element annotatedElement: elementsAnnotatedWith) {
			TypeElement domainObjectClass = getDomainTypeElement(annotatedElement);
			
			//Dto is going to be generated
			if (domainObjectClass.equals(element)) {
				return annotatedElement;
			}
		}
		
		return null;
	}

	protected MutableType toDto(TypeElement element, RoundEnvironment roundEnv) {
		
		Element configurationElement = getConfigurationElement(element, roundEnv);
		
		if (configurationElement != null) {
			return getDtoType(getNameTypes().toType(configurationElement));
		}
		
		return null;
	}
	
	protected String toMethod(String name) {
		
		if (name.length() < 2) {
			return name.toUpperCase();
		}
		
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	protected String toMethod(String prefix, String fieldName) {
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
	
	protected String toGetter(String fieldName) {
		return toMethod(GETTER_PREFIX, fieldName) + "()";
	}
	
	protected String toGetter(ExecutableElement method) {
		return toGetter(method.getSimpleName().toString());
	}

	protected String toSetter(String fieldName) {
		return toMethod(SETTER_PREFIX, fieldName);
	}
	
	protected String toSetter(ExecutableElement method) {
		if (method.getSimpleName().toString().startsWith(GETTER_PREFIX)) {
			return toSetter(method.getSimpleName().toString().substring(GETTER_PREFIX.length()));
		}
		return toSetter(method.getSimpleName().toString());
	}

	protected String toField(String fieldName) {
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
	
	protected String toField(ExecutableElement getterMethod) {
		
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

	public MutableType getDtoType(TypeElement typeElement) {
		MutableType mutableType = getNameTypes().toType(typeElement);
		
		return getDtoType((MutableType)genericsSupport.applyVariableGenerics(mutableType, 
				getDomainTypeElement(processingEnv.getElementUtils().getTypeElement(mutableType.getCanonicalName()))));
	}
	
	public static MutableType getDtoType(MutableType mutableType) {
		return mutableType.getSimpleName().endsWith(DEFAULT_SUFFIX) ?
				mutableType.setName(mutableType.getSimpleName().substring(0, mutableType.getSimpleName().length() - DEFAULT_SUFFIX.length())) :
				mutableType.addClassSufix(DTO_SUFFIX);
	}

	protected boolean isProcessed(List<String> generated, String fieldName) {
		return generated.contains(fieldName);
	}
	
	class PathResolver implements Iterator<String> {
		private String[] fields;
		private int index = 0;
		public PathResolver(String path) {
			fields = path.split("\\.");
			if (fields == null) {
				fields = new String[0];
			}
		}
		
		public boolean isNested() {
			return fields.length > 1;
		}
		
		@Override
		public boolean hasNext() {
			return index < fields.length;
		}

		@Override
		public String next() {
			return fields[index++];
		}
		
		@Override
		public void remove() {
			throw new RuntimeException("Remove operation is not allowed in the " + getClass().getSimpleName());
		}
		
		public void reset() {
			index = 0;
		}
	}
	
	protected String getFieldPath(ExecutableElement method) {
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

	protected ExecutableElement getDomainGetterMethod(Element element, String fieldName) {
		return getDomainMethod(element, new PathResolver(fieldName), GETTER_PREFIX);
	}

	protected ExecutableElement getDomainSetterMethod(Element element, String fieldName) {
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

	protected boolean hasSetterMethod(TypeElement element, ExecutableElement method) {

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

	protected boolean isIdField(String field) {
		return field.equals(ID_METHOD_NAME);
	}
	
	protected boolean isIdMethod(ExecutableElement method) {
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
	
	protected boolean isFieldIgnored(TypeElement configurationElement, String field) {
		
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

	protected ExecutableElement getDtoIdMethod(TypeElement configurationElement) {
		
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
	
	protected ExecutableElement getIdMethod(Element element) {
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

	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {
		
		TypeElement domainObjectClass = getDomainTypeElement(element);

		if (domainObjectClass == null) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to find domain class reference for " + element.toString(), element);
			return;
		}

		MappingType mappingType = MappingType.AUTOMATIC;
		Mapping mapping =  element.getAnnotation(Mapping.class);

		if (mapping != null) {
			mappingType = mapping.value();
		}

		for (ElementPrinter elementPrinter: getElementPrinters(pw)) {
			processMethods(element, domainObjectClass, mappingType, elementPrinter);
		}
	}

	protected TypeMirror erasure(TypeElement typeElement, TypeMirror param) {

		if (!param.getKind().equals(TypeKind.TYPEVAR)) {
			return param;
		}
		
		TypeVariable typeVar = (TypeVariable)param;
		if (typeVar.getUpperBound() != null) {
			NamedType convertedTypeVar = convertType(typeVar.getUpperBound());
			if (convertedTypeVar != null) {
				return typeVar.getUpperBound();
			}
		}

		if (typeVar.getLowerBound() != null) {
			NamedType convertedTypeVar = convertType(typeVar.getLowerBound());
			if (convertedTypeVar != null) {
				return typeVar.getLowerBound();
			}
		}

		//no success, let's iterate over the classes and find the bounds replacement
		String parameterName = typeVar.asElement().getSimpleName().toString();
		TypeMirror erasureSuperclass = erasureSuperclass(typeElement, parameterName, null);
		
		if (erasureSuperclass != null) {
			return erasureSuperclass;
		}
		
		return erasureInterfaces(typeElement, parameterName);
	}
	
	protected TypeMirror erasureInterfaces(TypeElement typeElement, String parameterName) {

		for (TypeMirror interfaceType: typeElement.getInterfaces()) {
			if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
				
				TypeElement currentTypeElement = (TypeElement)((DeclaredType)interfaceType).asElement();
				
				for (TypeParameterElement parameter: currentTypeElement.getTypeParameters()) {
					int i = 0;
					if (parameter.getSimpleName().toString().equals(parameterName)) {
						return ((DeclaredType)interfaceType).getTypeArguments().get(i);
					}
					i++;
				}
				
				erasureSuperclass(currentTypeElement, parameterName, typeElement);
			}
		}
		
		return null;
	}

	protected TypeMirror erasureSuperclass(TypeElement typeElement, String parameterName, TypeElement owner) {

		Element currentElement = typeElement;
		
		while (currentElement != null && currentElement.asType().getKind().equals(TypeKind.DECLARED)) {
			TypeElement currentTypeElement = (TypeElement)currentElement;
			for (TypeParameterElement parameter: currentTypeElement.getTypeParameters()) {
				int i = 0;
				if (parameter.getSimpleName().toString().equals(parameterName)) {
					if (owner != null) {
						DeclaredType dc = (DeclaredType)owner.getSuperclass();
						return dc.getTypeArguments().get(i);
					}
					i++;
				}
			}
			
			TypeMirror erasure = erasureInterfaces(currentTypeElement, parameterName);

			if (erasure != null) {
				return erasure;
			}
			
			owner = currentTypeElement;
			
			if (currentTypeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
				currentElement = ((DeclaredType)currentTypeElement.getSuperclass()).asElement();
			} else {
				currentElement = null;
			}
		}
		
		return null;
	}

	protected abstract ElementPrinter[] getElementPrinters(PrintWriter pw);
	protected abstract boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement);
	
	protected boolean initializeContext(ProcessorContext context) {

		context.setFieldName(toField(context.getMethod()));
		context.setDomainTypeElement(getDomainTypeElement(context.getConfigurationElement()));

		NamedType type = null;
			
		if (context.getMethod().getReturnType().getKind().equals(TypeKind.TYPEVAR)) {
			TypeMirror returnType = erasure(context.getDomainTypeElement(), context.getMethod().getReturnType());
			if (returnType != null) {
				type = getNameTypes().toType(returnType);
			} else {
				processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find erasure for the " + context.getDomainMethod().getReturnType().toString() + " in the method: " + context.getFieldName(), 
						context.getConfigurationElement());
				return false;
			}
		} else {
			type = getNameTypes().toType(context.getMethod().getReturnType());
		}

		TypeMirror domainReturnType = context.getDomainMethod().getReturnType();
		
		if (context.getDomainMethod().getReturnType().getKind().equals(TypeKind.TYPEVAR)) {
			TypeMirror erasedType = erasure(context.getDomainTypeElement(), domainReturnType);
			if (erasedType != null) {
				domainReturnType = erasedType;
			} else {
				TypeMirror targetEntityType = getTargetEntityType(context.getDomainMethod());
				if (targetEntityType != null && !context.getDomainMethod().getReturnType().equals(targetEntityType)) {
					domainReturnType = targetEntityType;
				}
			}
		}

		if (context.getMethod().getReturnType().getKind().equals(TypeKind.VOID)) {
			
			type = convertType(domainReturnType);

			if (type == null) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find DTO alternative for " + context.getDomainMethod().getReturnType().toString() + ". Skipping getter " + 
						context.getMethod().getSimpleName().toString(), context.getConfigurationElement());
				return false;
			}
		}

		context.setFieldType(type);

		context.setDomainFieldPath(getFieldPath(context.getMethod()));
		context.setDomainFieldName(toGetter(context.getDomainFieldPath()));

		context.setDomainMethodReturnType(domainReturnType);

		
		return true;
	}
	
	protected void processMethods(TypeElement typeElement, TypeElement domainObjectClass, MappingType mappingType, ElementPrinter printer) {

		printer.initialize(typeElement);
		
		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(typeElement.getEnclosedElements());
		List<ExecutableElement> methods = ElementFilter.methodsIn(domainObjectClass.getEnclosedElements());

		List<String> generated = new ArrayList<String>();

		ExecutableElement idMethod = null;

		for (ExecutableElement overridenMethod: overridenMethods) {

			Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
			if (ignoreAnnotation != null) {

				String fieldName = getFieldPath(overridenMethod);
				ExecutableElement domainMethod = getDomainGetterMethod(domainObjectClass, fieldName);

				if (isIdMethod(domainMethod)) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "Id method can't be ignored. There should be an id method available for merging purposes.", typeElement);
					return;
				}
				generated.add(getFieldPath(overridenMethod));
			}
		}

		for (ExecutableElement overridenMethod: overridenMethods) {

			String fieldName = getFieldPath(overridenMethod);
				
			if (!isProcessed(generated, fieldName)) {
				generated.add(fieldName);

				ExecutableElement domainMethod = getDomainGetterMethod(domainObjectClass, fieldName);

				if (domainMethod == null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find method " + overridenMethod.getSimpleName().toString() + 
							" in the domain class " + domainObjectClass.toString(), typeElement);
					continue;
				}

				if (isIdMethod(domainMethod)) {
					idMethod = domainMethod;
				}

				ProcessorContext context = new ProcessorContext(typeElement, Modifier.PUBLIC, overridenMethod, domainMethod);
				if (!initializeContext(context)) {
					continue;
				}
				printer.print(context);					
				
				PathResolver pathResolver = new PathResolver(fieldName);
				String currentPath = pathResolver.next();
				String fullPath = currentPath;
				
				if (pathResolver.hasNext()) {

					Element currentElement = domainObjectClass;
	
					while (pathResolver.hasNext()) {
						ExecutableElement fieldGetter = getDomainGetterMethod(currentElement, currentPath);
						
						if (fieldGetter.getReturnType().getKind().equals(TypeKind.DECLARED)) {
							currentElement = ((DeclaredType)fieldGetter.getReturnType()).asElement();
							ExecutableElement nestedIdMethod = getIdMethod(currentElement);

							if (nestedIdMethod == null) {
								//TODO Check @Id annotation is the configuration - nested field names
								processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find id method in the class " + fieldGetter.getReturnType().toString() +
										". If the class/interface does not have strictly specified ID, please specify the id in the configuration using " + 
										Id.class.getCanonicalName() + " annotation.", typeElement);
							} else {

								//TODO Check if is not already generated
								context = new ProcessorContext(typeElement, Modifier.PROTECTED, nestedIdMethod, nestedIdMethod);
								if (!initializeContext(context)) {
									continue;
								}

								context.setDomainFieldPath(fullPath + "." + toField(nestedIdMethod));
								context.setFieldName(toField(context.getDomainFieldPath()));
								context.setDomainFieldName(toGetter(context.getDomainFieldPath()));

								printer.print(context);
							}
						} else {
							if (pathResolver.hasNext()) {
								processingEnv.getMessager().printMessage(Kind.ERROR, "Invalid mapping specified in the field " + fieldName + ". Current path (" + 
										currentPath + ") address getter type that is not class/interfaces (" + fieldGetter.getReturnType().toString() + ")." +
										"You probably mistyped this field in the configuration.", typeElement);
							}
						}

						currentPath = pathResolver.next();
						fullPath += "." + currentPath;
						
					}
				}
			}
		}

		if (mappingType.equals(MappingType.AUTOMATIC)) {
			for (ExecutableElement method: methods) {
				
				String fieldName = getFieldPath(method);
				
				if (!isProcessed(generated, fieldName) && method.getSimpleName().toString().startsWith(GETTER_PREFIX) && hasSetterMethod(domainObjectClass, method) && method.getModifiers().contains(Modifier.PUBLIC)) {

					generated.add(fieldName);

					if (isIdMethod(method)) {
						idMethod = method;
					}

					ProcessorContext context = new ProcessorContext(typeElement, Modifier.PUBLIC, method, method);
					if (!initializeContext(context)) {
						continue;
					}

					printer.print(context);
				}			
			}
		}
		
		if (idMethod == null) {

			idMethod = getIdMethod(domainObjectClass);
			
			if (idMethod != null) {
				ProcessorContext context = new ProcessorContext(typeElement, Modifier.PROTECTED, idMethod, idMethod);
				if (initializeContext(context)) {
					printer.print(context);
				} else {
					idMethod = null;
				}
			}
		}

		for (ExecutableElement overridenMethod: overridenMethods) {
			if (!isProcessed(generated, getFieldPath(overridenMethod)) && isIdMethod(overridenMethod)) {
				if (idMethod != null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "Multiple identifier methods were specified." + 
							idMethod.getSimpleName().toString() + " in the " + idMethod.getEnclosingElement().toString() + " class and " +
							overridenMethod.getSimpleName().toString() + " in the configuration " + typeElement.toString(), typeElement);
					return;
				} else {
					idMethod = overridenMethod;
				}
			}
		}
		
		if (idMethod == null && shouldHaveIdMethod(typeElement, domainObjectClass)) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "Identifier method could not be found in the automatic way. Please specify id method using " + 
					Id.class.getSimpleName() + " annotation or just specify id as a method name.", typeElement);
			return;
		}

		printer.finish(typeElement);
	}	
}