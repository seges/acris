package sk.seges.sesam.pap.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.annotation.Id;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.printer.api.ElementPrinter;
import sk.seges.sesam.pap.model.provider.TransferObjectProcessorContextProvider;
import sk.seges.sesam.pap.model.resolver.DefaultEntityResolver;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

public abstract class AbstractTransferProcessor extends AbstractConfigurableProcessor {

	protected TransferObjectHelper toHelper;
	protected TransferObjectProcessorContextProvider processorContextProvider;
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(TransferObjectMapping.class.getCanonicalName());
		return annotations;
	}
	
	protected EntityResolver getEntityResolver() {
		return new DefaultEntityResolver();
	}

	protected boolean isProcessed(List<String> generated, String fieldName) {
		return generated.contains(fieldName);
	}

	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, FormattedPrintWriter pw) {
				
		ConfigurationTypeElement configurationElement = new ConfigurationTypeElement(element, processingEnv, roundEnv);

		if (configurationElement.getDomainTypeElement() == null) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to find domain class reference for " + element.toString(), element);
			return;
		}

		MappingType mappingType = MappingType.AUTOMATIC;
		Mapping mapping =  element.getAnnotation(Mapping.class);

		if (mapping != null) {
			mappingType = mapping.value();
		}

		for (ElementPrinter elementPrinter: getElementPrinters(pw)) {
			elementPrinter.initialize(configurationElement, outputName);
			processMethods(configurationElement, mappingType, elementPrinter);
		}
	}

	protected abstract ElementPrinter[] getElementPrinters(FormattedPrintWriter pw);
	
	protected boolean checkPreconditions(Element element, NamedType outputName, boolean alreadyExists) {
		return new ConfigurationTypeElement((TypeElement)element, processingEnv, roundEnv).getDelegateConfigurationTypeElement() == null;
	}

	protected TransferObjectProcessorContextProvider getProcessorContextProvider(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		return new TransferObjectProcessorContextProvider(processingEnv, roundEnv, getEntityResolver());
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		this.toHelper = new TransferObjectHelper(getNameTypes(), processingEnv, roundEnv, methodHelper);
		processorContextProvider = getProcessorContextProvider(processingEnv, roundEnv);
		
		return super.process(annotations, roundEnv);
	}
		
	protected void processMethods(ConfigurationTypeElement configurationTypeElement, MappingType mappingType, ElementPrinter printer) {

		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(configurationTypeElement.asElement().getEnclosedElements());
		
		DomainTypeElement domainTypeElement = configurationTypeElement.getDomainTypeElement();

		TypeElement processingElement = domainTypeElement.asElement();
	
		List<String> generated = new ArrayList<String>();
		
		ExecutableElement idMethod = null;

		for (ExecutableElement overridenMethod: overridenMethods) {
			
			Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
			if (ignoreAnnotation != null) {

				String fieldName = toHelper.getFieldPath(overridenMethod);
				ExecutableElement domainMethod = toHelper.getDomainGetterMethod(processingElement, fieldName);

				if (getEntityResolver().isIdMethod(domainMethod)) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Id method can't be ignored. There should be an id method available for merging purposes.", configurationTypeElement.asElement());
					return;
				}
				generated.add(toHelper.getFieldPath(overridenMethod));
			}
		}

		for (ExecutableElement overridenMethod: overridenMethods) {

			String fieldName = toHelper.getFieldPath(overridenMethod);
			
			if (!isProcessed(generated, fieldName)) {
				generated.add(fieldName);

				ExecutableElement domainMethod = toHelper.getDomainGetterMethod(((DeclaredType)domainTypeElement.asType()).asElement(), fieldName);

				if (domainMethod == null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find method " + overridenMethod.getSimpleName().toString() + 
							" in the domain class " + domainTypeElement.getCanonicalName(), configurationTypeElement.asElement());
					continue;
				}

				if (getEntityResolver().isIdMethod(domainMethod)) {
					idMethod = domainMethod;
				}

				ProcessorContext context = processorContextProvider.get(configurationTypeElement, Modifier.PUBLIC, overridenMethod, domainMethod);
				if (context == null) {
					continue;
				}
				printer.print(context);					
				
				PathResolver pathResolver = new PathResolver(fieldName);
				String currentPath = pathResolver.next();
				String fullPath = currentPath;
				
				if (pathResolver.hasNext()) {

					Element currentElement = processingElement;
	
					while (pathResolver.hasNext()) {
						ExecutableElement fieldGetter = toHelper.getDomainGetterMethod(currentElement, currentPath);
						
						if (fieldGetter.getReturnType().getKind().equals(TypeKind.DECLARED)) {
							currentElement = ((DeclaredType)fieldGetter.getReturnType()).asElement();
							ExecutableElement nestedIdMethod = toHelper.getIdMethod((DeclaredType)fieldGetter.getReturnType(), getEntityResolver());

							if (nestedIdMethod == null) {
								
								if ((!currentElement.getKind().equals(ElementKind.CLASS) &&
									!currentElement.getKind().equals(ElementKind.INTERFACE)) ||
										getEntityResolver().shouldHaveIdMethod(configurationTypeElement, fieldGetter.getReturnType())) {
									//TODO Check @Id annotation is the configuration - nested field names
									processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method in the class " + fieldGetter.getReturnType().toString() +
											". If the class/interface does not have strictly specified ID, please specify the id in the configuration using " + 
											Id.class.getCanonicalName() + " annotation.", configurationTypeElement.asElement());
								}
							} else {

								//TODO Check if is not already generated
								context = processorContextProvider.get(configurationTypeElement, Modifier.PROTECTED, nestedIdMethod, nestedIdMethod, fullPath);
								if (context == null) {
									continue;
								}

								printer.print(context);
							}
						} else {
							if (pathResolver.hasNext()) {
								processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Invalid mapping specified in the field " + fieldName + ". Current path (" + 
										currentPath + ") address getter type that is not class/interfaces (" + fieldGetter.getReturnType().toString() + ")." +
										"You probably mistyped this field in the configuration.", configurationTypeElement.asElement());
							}
						}

						currentPath = pathResolver.next();
						fullPath += "." + currentPath;
						
					}
				}
			}
		}

		while (processingElement != null) {

			List<ExecutableElement> methods = ElementFilter.methodsIn(processingElement.getEnclosedElements());
	
			if (mappingType.equals(MappingType.AUTOMATIC)) {
				for (ExecutableElement method: methods) {
					
					String fieldName = toHelper.getFieldPath(method);
					
					if (!isProcessed(generated, fieldName) && toHelper.isGetterMethod(method) && 
							toHelper.hasSetterMethod(domainTypeElement.asElement(), method) && method.getModifiers().contains(Modifier.PUBLIC)) {
	
						generated.add(fieldName);
	
						if (getEntityResolver().isIdMethod(method)) {
							idMethod = method;
						}

						ProcessorContext context = processorContextProvider.get(configurationTypeElement, Modifier.PUBLIC, method, method);
						if (context == null) {
							continue;
						}

						printer.print(context);
					}			
				}
			}
							
			if (processingElement.getSuperclass() != null && processingElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
				processingElement = (TypeElement)((DeclaredType)processingElement.getSuperclass()).asElement();
			} else {
				processingElement = null;
			}
		}

		if (idMethod == null && domainTypeElement.asType().getKind().equals(TypeKind.DECLARED)) {
			
			idMethod = toHelper.getIdMethod((DeclaredType) domainTypeElement.asType(), getEntityResolver());
			
			if (idMethod != null) {

				ProcessorContext context = processorContextProvider.get(configurationTypeElement, Modifier.PROTECTED, idMethod, idMethod);
				if (context != null) {
					printer.print(context);
				} else {
					idMethod = null;
				}
			}
		}

		for (ExecutableElement overridenMethod: overridenMethods) {
			if (!isProcessed(generated, toHelper.getFieldPath(overridenMethod)) && getEntityResolver().isIdMethod(overridenMethod)) {
				if (idMethod != null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Multiple identifier methods were specified." + 
							idMethod.getSimpleName().toString() + " in the " + idMethod.getEnclosingElement().toString() + " class and " +
							overridenMethod.getSimpleName().toString() + " in the configuration " + configurationTypeElement.getCanonicalName(), 
							configurationTypeElement.asElement());
					return;
				} else {
					idMethod = overridenMethod;
				}
			}
		}

		if (idMethod == null && getEntityResolver().shouldHaveIdMethod(configurationTypeElement, domainTypeElement.asType())) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Identifier method could not be found in the automatic way. Please specify id method using " + 
					Id.class.getSimpleName() + " annotation or just specify id as a method name.", configurationTypeElement.asElement());
			return;
		}

		printer.finish(configurationTypeElement);
	}
}