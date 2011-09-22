package sk.seges.sesam.pap.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
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

		if (configurationElement.getDomain() == null) {
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
		this.toHelper = new TransferObjectHelper(getNameTypes(), processingEnv, roundEnv);
		processorContextProvider = getProcessorContextProvider(processingEnv, roundEnv);
		
		return super.process(annotations, roundEnv);
	}
	
	protected void processMethods(ConfigurationTypeElement configurationTypeElement, MappingType mappingType, ElementPrinter printer) {

		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(configurationTypeElement.asElement().getEnclosedElements());
		
		DomainTypeElement domainTypeElement = configurationTypeElement.getDomain();

		DomainTypeElement processingElement = domainTypeElement;
	
		List<String> generated = new ArrayList<String>();
		
		for (ExecutableElement overridenMethod: overridenMethods) {
			
			Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
			if (ignoreAnnotation != null) {

				String fieldName = TransferObjectHelper.getFieldPath(overridenMethod);
				ExecutableElement domainMethod = processingElement.getGetterMethod(fieldName);

				if (getEntityResolver().isIdMethod(domainMethod)) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Id method can't be ignored. There should be an id method available for merging purposes.", configurationTypeElement.asElement());
					return;
				}
				generated.add(TransferObjectHelper.getFieldPath(overridenMethod));
			}
		}

		for (ExecutableElement overridenMethod: overridenMethods) {

			String fieldName = TransferObjectHelper.getFieldPath(overridenMethod);
			
			if (!isProcessed(generated, fieldName)) {
				generated.add(fieldName);

				ExecutableElement domainMethod = domainTypeElement.getGetterMethod(fieldName);

				if (domainMethod == null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find method " + overridenMethod.getSimpleName().toString() + 
							" in the domain class " + domainTypeElement.getCanonicalName(), configurationTypeElement.asElement());
					continue;
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

					DomainTypeElement currentElement = processingElement;
	
					while (pathResolver.hasNext()) {
						currentElement = currentElement.getDomainReference(currentPath);
						
						if (currentElement != null) {
							ExecutableElement nestedIdMethod = currentElement.getIdMethod(getEntityResolver());

							if (nestedIdMethod == null && getEntityResolver().shouldHaveIdMethod(currentElement)) {
								//TODO Check @Id annotation is the configuration - nested field names
								processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method in the class " + currentElement.getCanonicalName() +
										". If the class/interface does not have strictly specified ID, please specify the id in the configuration using " + 
										Id.class.getCanonicalName() + " annotation.", configurationTypeElement.asElement());
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
										currentPath + ") address getter type that is not class/interfaces." +
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

			List<ExecutableElement> methods = ElementFilter.methodsIn(processingElement.asElement().getEnclosedElements());
	
			if (mappingType.equals(MappingType.AUTOMATIC)) {
				for (ExecutableElement method: methods) {
					
					String fieldName = TransferObjectHelper.getFieldPath(method);
					
					if (!isProcessed(generated, fieldName) && toHelper.isGetterMethod(method) && 
							toHelper.hasSetterMethod(domainTypeElement.asElement(), method) && method.getModifiers().contains(Modifier.PUBLIC)) {
	
						generated.add(fieldName);
	
						ProcessorContext context = processorContextProvider.get(configurationTypeElement, Modifier.PUBLIC, method, method);
						if (context == null) {
							continue;
						}

						printer.print(context);
					}			
				}
			}

			processingElement = processingElement.getSuperClass();
		}

		ExecutableElement idMethod = domainTypeElement.getIdMethod(getEntityResolver());
		
		if (idMethod == null && getEntityResolver().shouldHaveIdMethod(domainTypeElement)) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Identifier method could not be found in the automatic way. Please specify id method using " + 
					Id.class.getSimpleName() + " annotation or just specify id as a method name.", configurationTypeElement.asElement());
			return;
		} else if (idMethod != null && !isProcessed(generated, MethodHelper.toField(idMethod))) {
			ProcessorContext context = processorContextProvider.get(configurationTypeElement, Modifier.PROTECTED, idMethod, idMethod);
			if (context != null) {
				printer.print(context);
			}
		}

		printer.finish(configurationTypeElement);
	}
}