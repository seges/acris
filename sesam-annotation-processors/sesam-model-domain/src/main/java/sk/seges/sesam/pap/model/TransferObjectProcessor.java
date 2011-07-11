package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.model.annotation.Field;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectConfiguration;


public class TransferObjectProcessor extends AbstractConfigurableProcessor {

	private static final String DEFAULT_SUFFIX = "Configuration";
	private static final String DTO_SUFFIX = "Dto";
	private static final String SETTER_PREFIX = "set";
	private static final String GETTER_PREFIX = "get";
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(TransferObjectConfiguration.class.getCanonicalName());
		return annotations;
	}
	
	@Override
	protected Type[] getImports(TypeElement typeElement) {
		MutableType dtoSuperclass = getDtoSuperclass(typeElement);
		if (dtoSuperclass != null) {
			return new Type[] {dtoSuperclass};
		}
		return super.getImports(typeElement);
	}

	private MutableType getDtoSuperclass(TypeElement typeElement) {

		TypeElement domainObjectClass = getDomainClass(typeElement);

		if (domainObjectClass != null) {
			TypeMirror superClassType = domainObjectClass.getSuperclass();
			
			if (superClassType.getKind().equals(TypeKind.DECLARED)) {
				
				MutableType dto = toDto(((TypeElement)((DeclaredType)superClassType).asElement()), roundEnv);
				if (dto != null) {
					return dto;
				}
			}
		}		
		
		return null;
	}
	
	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {
		switch (type) {
			case OUTPUT_SUPERCLASS:
				MutableType dtoSuperclass = getDtoSuperclass(typeElement);
				if (dtoSuperclass != null) {
					
					TypeElement domainObjectClass = getDomainClass(typeElement);

					if (domainObjectClass != null) {
						TypeMirror superClassType = domainObjectClass.getSuperclass();
						return new Type[] { genericsSupport.applyGenerics(dtoSuperclass, (DeclaredType)superClassType) };
					}
					
					return new Type[] {dtoSuperclass};
				}

		}
		return super.getConfigurationTypes(type, typeElement);
	}
	
	public static MutableType getOutputClass(MutableType mutableType) {
		
		return mutableType.getSimpleName().endsWith(DEFAULT_SUFFIX) ?
				mutableType.setName(mutableType.getSimpleName().substring(0, mutableType.getSimpleName().length() - DEFAULT_SUFFIX.length())) :
				mutableType.addClassSufix(DTO_SUFFIX);
	}
	
	protected TypeElement getDomainClass(Element configurationElement) {
		TransferObjectConfiguration transferObjectConfiguration =  configurationElement.getAnnotation(TransferObjectConfiguration.class);
		TypeElement domainObjectClass = AnnotationClassPropertyHarvester.getTypeOfClassProperty(transferObjectConfiguration, new AnnotationClassProperty<TransferObjectConfiguration>() {

			@Override
			public Class<?> getClassProperty(TransferObjectConfiguration annotation) {
				return annotation.value();
			}
		});

		return domainObjectClass;
	}
	
	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
		return new NamedType[] {
				getOutputClass((MutableType)genericsSupport.applyVariableGenerics(mutableType, getDomainClass(processingEnv.getElementUtils().getTypeElement(mutableType.getCanonicalName()))))
		};
	}

	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {
		
		TypeElement domainObjectClass = getDomainClass(element);

		if (domainObjectClass == null) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to find domain class reference for " + element.toString(), element);
			return;
		}

		MappingType mappingType = MappingType.AUTOMATIC;
		Mapping mapping =  element.getAnnotation(Mapping.class);

		if (mapping != null) {
			mappingType = mapping.value();
		}
		
		processDomainMethods(element, domainObjectClass, mappingType, pw, roundEnv);
	}
	
	private void generateFields(TypeElement typeElement, TypeElement domainObjectClass, MappingType mappingType, PrintWriter pw, RoundEnvironment roundEnv) {
		
		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(typeElement.getEnclosedElements());
		List<ExecutableElement> methods = ElementFilter.methodsIn(domainObjectClass.getEnclosedElements());

		List<String> generated = new ArrayList<String>();

		for (ExecutableElement overridenMethod: overridenMethods) {

			Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
			if (ignoreAnnotation != null) {
				generated.add(overridenMethod.getSimpleName().toString());
			}
		}

		for (ExecutableElement overridenMethod: overridenMethods) {
			Field fieldAnnotation = overridenMethod.getAnnotation(Field.class);

			//TODO compare types - if it is assignable
			if (fieldAnnotation != null && !isProcessed(generated, overridenMethod)) {
				if (overridenMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
					ExecutableElement domainMethod = getDomainGetterMethod(domainObjectClass, overridenMethod);
					if (domainMethod == null) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find method " + overridenMethod.getSimpleName().toString() + 
								" in the domain class " + domainObjectClass.toString(), typeElement);
						generated.add(overridenMethod.getSimpleName().toString());
						continue;
					} else {
						pw.print("private " + domainMethod.getReturnType().toString());
					}
				} else {
					pw.print("private " + overridenMethod.getReturnType().toString());
				}
				pw.println(" " + toField(overridenMethod) + ";");
				pw.println();
				generated.add(overridenMethod.getSimpleName().toString());
			}
		}

		if (mappingType.equals(MappingType.AUTOMATIC)) {
			for (ExecutableElement method: methods) {
				if (!isProcessed(generated, method) && method.getSimpleName().toString().startsWith(GETTER_PREFIX) && hasSetterMethod(domainObjectClass, method) && method.getModifiers().contains(Modifier.PUBLIC)) {
					
					String returnType = method.getReturnType().toString();
					
					if (method.getReturnType().getKind().equals(TypeKind.DECLARED)) {
						MutableType dto = toDto((TypeElement)((DeclaredType)method.getReturnType()).asElement(), roundEnv);
						if (dto != null) {
							returnType = dto.getCanonicalName();
						}
					}
					
					pw.println("private " + returnType + " " + toField(method) + ";");
					pw.println();
				}
			}
		}
	}
	
	private MutableType toDto(TypeElement element, RoundEnvironment roundEnv) {
		
		if (element.asType().getKind().isPrimitive()) {
			//primitives cannot be cast to DTO
			return null;
		}
		MutableType outputDtoClass = getOutputClass(getNameTypes().toType(element));
		TypeElement typeDtoElement = processingEnv.getElementUtils().getTypeElement(outputDtoClass.getCanonicalName());
		if (typeDtoElement != null) {
			return outputDtoClass;
		}
		
		Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(TransferObjectConfiguration.class);
		for (Element annotatedElement: elementsAnnotatedWith) {
			TypeElement domainObjectClass = getDomainClass(annotatedElement);
			
			//Dto is going to be generated
			if (domainObjectClass.equals(element)) {
				return getOutputClass(getNameTypes().toType(annotatedElement));
			}
		}
		
		return null;
	}
	
	private void generateMethods(TypeElement typeElement, TypeElement domainObjectClass, MappingType mappingType, PrintWriter pw, RoundEnvironment roundEnv) {

		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(typeElement.getEnclosedElements());
		List<ExecutableElement> methods = ElementFilter.methodsIn(domainObjectClass.getEnclosedElements());

		List<String> generated = new ArrayList<String>();

		for (ExecutableElement overridenMethod: overridenMethods) {

			Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
			if (ignoreAnnotation != null) {
				generated.add(overridenMethod.getSimpleName().toString());
			}
		}

		for (ExecutableElement overridenMethod: overridenMethods) {
			Field fieldAnnotation = overridenMethod.getAnnotation(Field.class);

			//TODO compare types - if it is assignable
			if (fieldAnnotation != null && !isProcessed(generated, overridenMethod)) {
				String fieldName = toField(overridenMethod);

				String type = overridenMethod.getReturnType().toString();
				
				if (overridenMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
					ExecutableElement domainMethod = getDomainGetterMethod(domainObjectClass, overridenMethod);
					if (domainMethod == null) {
						generated.add(overridenMethod.getSimpleName().toString());
						continue;
					} else {
						type = domainMethod.getReturnType().toString();
					}
				}

				pw.println("public " + type + " " + toGetter(overridenMethod) + "() {");
				pw.println("return " + fieldName + ";");
				pw.println("}");
				pw.println();
				pw.println("public void " + toSetter(overridenMethod) + "(" + type + " " + fieldName + ") {");
				pw.println("this." + fieldName + " = " + fieldName + ";");
				pw.println("}");
				pw.println();
				generated.add(overridenMethod.getSimpleName().toString());
			}
		}
		
		if (mappingType.equals(MappingType.AUTOMATIC)) {
			for (ExecutableElement method: methods) {
				if (!isProcessed(generated, method) && method.getSimpleName().toString().startsWith(GETTER_PREFIX) && hasSetterMethod(domainObjectClass, method)) {
					String fieldName = toField(method);

					String returnType = method.getReturnType().toString();
					
					if (method.getReturnType().getKind().equals(TypeKind.DECLARED)) {
						MutableType dto = toDto((TypeElement)((DeclaredType)method.getReturnType()).asElement(), roundEnv);
						if (dto != null) {
							returnType = dto.getCanonicalName();
						}
					}

					pw.println("public " + returnType + " " + method.getSimpleName().toString() + "() {");
					pw.println("return " + fieldName + ";");
					pw.println("}");
					pw.println();
					pw.println("public void " + toSetter(method) + "(" + returnType + " " + fieldName + ") {");
					pw.println("this." + fieldName + " = " + fieldName + ";");
					pw.println("}");
					pw.println();
					generated.add(method.getSimpleName().toString().substring(GETTER_PREFIX.length()));
				}
			}
		}
	}
	
	private void processDomainMethods(TypeElement typeElement, TypeElement domainObjectClass, MappingType mappingType, PrintWriter pw, RoundEnvironment roundEnv) {
		generateFields(typeElement, domainObjectClass, mappingType, pw, roundEnv);
		generateMethods(typeElement, domainObjectClass, mappingType, pw, roundEnv);
	}
	
	private boolean hasSetterMethod(TypeElement element, ExecutableElement method) {

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

	private ExecutableElement getDomainGetterMethod(TypeElement element, ExecutableElement method) {

		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		for (ExecutableElement elementMethod: methods) {
			
			if (elementMethod.getModifiers().contains(Modifier.PUBLIC) && 
				elementMethod.getSimpleName().toString().equals(GETTER_PREFIX + toMethod(method.getSimpleName().toString()))) {
				return elementMethod;
			}
		}
		
		if (element.getSuperclass() != null && element.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			return getDomainGetterMethod((TypeElement)((DeclaredType)element.getSuperclass()).asElement(), method);
		}
		
		return null;
		
	}

	private boolean isProcessed(List<String> generated, ExecutableElement method) {
		if (method.getSimpleName().toString().startsWith(GETTER_PREFIX)) {
			return generated.contains(toField(method));
		}
		
		return generated.contains(method.getSimpleName().toString());
	}
	
	private String toField(ExecutableElement getterMethod) {
		
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

	private String toMethod(String name) {
		
		if (name.length() < 2) {
			return name.toUpperCase();
		}
		
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private String toGetter(ExecutableElement method) {
		return GETTER_PREFIX + toMethod(method.getSimpleName().toString());
	}
	
	private String toSetter(ExecutableElement method) {
		if (method.getSimpleName().toString().startsWith(GETTER_PREFIX)) {
			return SETTER_PREFIX + method.getSimpleName().toString().substring(GETTER_PREFIX.length());
		}
		return SETTER_PREFIX + toMethod(method.getSimpleName().toString());
	}
}