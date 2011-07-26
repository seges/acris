package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
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
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.annotation.Id;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.ElementPrinter;
import sk.seges.sesam.pap.model.model.PathResolver;
import sk.seges.sesam.pap.model.model.ProcessorContext;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

public abstract class AbstractTransferProcessor extends AbstractConfigurableProcessor {

	protected TransferObjectHelper toHelper;
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(TransferObjectMapping.class.getCanonicalName());
		return annotations;
	}
	
	protected TypeMirror getTargetEntityType(ExecutableElement method) {
		return method.getReturnType();
	}

	public ImmutableType getDtoType(TypeElement typeElement) {
		NamedType mutableType = getNameTypes().toType(typeElement);
		
		return TransferObjectHelper.getDtoType((ImmutableType)genericsSupport.applyVariableGenerics(mutableType, 
				toHelper.getDomainTypeElement(processingEnv.getElementUtils().getTypeElement(mutableType.getCanonicalName()))));
	}
	
	protected boolean isProcessed(List<String> generated, String fieldName) {
		return generated.contains(fieldName);
	}

	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {
		
		TypeElement domainObjectClass = toHelper.getDomainTypeElement(element);

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

	protected abstract ElementPrinter[] getElementPrinters(PrintWriter pw);
	
	protected abstract boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement);

	protected TypeMirror erasure(TypeElement typeElement, TypeMirror param) {

		if (!param.getKind().equals(TypeKind.TYPEVAR)) {
			return param;
		}
		
		TypeVariable typeVar = (TypeVariable)param;
		if (typeVar.getUpperBound() != null) {
			NamedType convertedTypeVar = toHelper.convertType(typeVar.getUpperBound());
			if (convertedTypeVar != null) {
				return typeVar.getUpperBound();
			}
		}

		if (typeVar.getLowerBound() != null) {
			NamedType convertedTypeVar = toHelper.convertType(typeVar.getLowerBound());
			if (convertedTypeVar != null) {
				return typeVar.getLowerBound();
			}
		}

		return ProcessorUtils.erasure(typeElement, typeVar);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		this.toHelper = new TransferObjectHelper(getNameTypes(), processingEnv, roundEnv);
		return super.process(annotations, roundEnv);
	}
	
	protected boolean initializeContext(ProcessorContext context) {

		context.setFieldName(toHelper.toField(context.getMethod()));
		context.setDomainTypeElement(toHelper.getDomainTypeElement(context.getConfigurationElement()));

		NamedType type = null;
			
		if (getTargetEntityType(context.getMethod()).getKind().equals(TypeKind.TYPEVAR)) {
			TypeMirror returnType = erasure(context.getDomainTypeElement(), getTargetEntityType(context.getMethod()));
			if (returnType != null) {
				type = getNameTypes().toType(returnType);
			} else {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find erasure for the " + getTargetEntityType(context.getDomainMethod()).toString() + " in the method: " + context.getFieldName(), 
						context.getConfigurationElement());
				return false;
			}
		} else {
			type = getNameTypes().toType(getTargetEntityType(context.getMethod()));
		}

		TypeMirror domainReturnType = getTargetEntityType(context.getDomainMethod());
		
		if (getTargetEntityType(context.getDomainMethod()).getKind().equals(TypeKind.TYPEVAR)) {
			TypeMirror erasedType = erasure(context.getDomainTypeElement(), domainReturnType);
			if (erasedType != null) {
				domainReturnType = erasedType;
			}/* else {
				TypeMirror targetEntityType = getTargetEntityType(context.getDomainMethod());
				if (targetEntityType != null && !context.getDomainMethod().getReturnType().equals(targetEntityType)) {
					domainReturnType = targetEntityType;
				}
			}*/
		}

		if (context.getMethod().getReturnType().getKind().equals(TypeKind.VOID)) {
			
			type = toHelper.convertType(domainReturnType);

			if (type == null) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find DTO alternative for " + getTargetEntityType(context.getDomainMethod()).toString() + ". Skipping getter " + 
						context.getMethod().getSimpleName().toString(), context.getConfigurationElement());
				return false;
			}
		}

		context.setFieldType(type);

		context.setDomainFieldPath(toHelper.getFieldPath(context.getMethod()));
		context.setDomainFieldName(toHelper.toGetter(context.getDomainFieldPath()));

		context.setDomainMethodReturnType(domainReturnType);

		if (domainReturnType.getKind().equals(TypeKind.DECLARED)) {
			
			NamedType domainReturnNamedType = getNameTypes().toType(domainReturnType);
			
			if (!type.getCanonicalName().equals(domainReturnNamedType.getCanonicalName())) {
				
				ImmutableType dtoType = null;
				
				if (domainReturnType.getKind().equals(TypeKind.DECLARED)) {
					dtoType = toHelper.toDto((TypeElement)((DeclaredType)domainReturnType).asElement(), roundEnv);
				}
	
				if (dtoType == null || !dtoType.getCanonicalName().equals(type.getCanonicalName())) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Return type from domain method " + domainReturnType.toString() + " " + context.getDomainTypeElement().toString() + 
							"." + context.getDomainFieldName() + " is not compatible with specified return type in the DTO " + type.toString() + ". Please, check your configuration " + 
							context.getConfigurationElement().toString(), context.getConfigurationElement());
					return false;
				}
			}
		} else {
			if (!type.getCanonicalName().equals(domainReturnType.toString())) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Return type from domain method " + domainReturnType.toString() + " " + context.getDomainTypeElement().toString() + 
						"." + context.getDomainFieldName() + " is not compatible with specified return type in the DTO " + type.toString() + ". Please, check your configuration " + 
						context.getConfigurationElement().toString(), context.getConfigurationElement());
				return false;
			}
		}
		
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

				String fieldName = toHelper.getFieldPath(overridenMethod);
				ExecutableElement domainMethod = toHelper.getDomainGetterMethod(domainObjectClass, fieldName);

				if (toHelper.isIdMethod(domainMethod)) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Id method can't be ignored. There should be an id method available for merging purposes.", typeElement);
					return;
				}
				generated.add(toHelper.getFieldPath(overridenMethod));
			}
		}

		for (ExecutableElement overridenMethod: overridenMethods) {

			String fieldName = toHelper.getFieldPath(overridenMethod);
			
			if (!isProcessed(generated, fieldName)) {
				generated.add(fieldName);

				ExecutableElement domainMethod = toHelper.getDomainGetterMethod(domainObjectClass, fieldName);

				if (domainMethod == null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find method " + overridenMethod.getSimpleName().toString() + 
							" in the domain class " + domainObjectClass.toString(), typeElement);
					continue;
				}

				if (toHelper.isIdMethod(domainMethod)) {
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
						ExecutableElement fieldGetter = toHelper.getDomainGetterMethod(currentElement, currentPath);
						
						if (fieldGetter.getReturnType().getKind().equals(TypeKind.DECLARED)) {
							currentElement = ((DeclaredType)fieldGetter.getReturnType()).asElement();
							ExecutableElement nestedIdMethod = toHelper.getIdMethod(currentElement);

							if (nestedIdMethod == null) {
								//TODO Check @Id annotation is the configuration - nested field names
								processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method in the class " + fieldGetter.getReturnType().toString() +
										". If the class/interface does not have strictly specified ID, please specify the id in the configuration using " + 
										Id.class.getCanonicalName() + " annotation.", typeElement);
							} else {

								//TODO Check if is not already generated
								context = new ProcessorContext(typeElement, Modifier.PROTECTED, nestedIdMethod, nestedIdMethod);
								if (!initializeContext(context)) {
									continue;
								}

								context.setDomainFieldPath(fullPath + "." + toHelper.toField(nestedIdMethod));
								context.setFieldName(toHelper.toField(context.getDomainFieldPath()));
								context.setDomainFieldName(toHelper.toGetter(context.getDomainFieldPath()));

								printer.print(context);
							}
						} else {
							if (pathResolver.hasNext()) {
								processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Invalid mapping specified in the field " + fieldName + ". Current path (" + 
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
				
				String fieldName = toHelper.getFieldPath(method);
				
				if (!isProcessed(generated, fieldName) && toHelper.isGetterMethod(method) && toHelper.hasSetterMethod(domainObjectClass, method) && method.getModifiers().contains(Modifier.PUBLIC)) {

					generated.add(fieldName);

					if (toHelper.isIdMethod(method)) {
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

			idMethod = toHelper.getIdMethod(domainObjectClass);
			
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
			if (!isProcessed(generated, toHelper.getFieldPath(overridenMethod)) && toHelper.isIdMethod(overridenMethod)) {
				if (idMethod != null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Multiple identifier methods were specified." + 
							idMethod.getSimpleName().toString() + " in the " + idMethod.getEnclosingElement().toString() + " class and " +
							overridenMethod.getSimpleName().toString() + " in the configuration " + typeElement.toString(), typeElement);
					return;
				} else {
					idMethod = overridenMethod;
				}
			}
		}
		
		if (idMethod == null && shouldHaveIdMethod(typeElement, domainObjectClass)) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Identifier method could not be found in the automatic way. Please specify id method using " + 
					Id.class.getSimpleName() + " annotation or just specify id as a method name.", typeElement);
			return;
		}

		printer.finish(typeElement);
	}	
}