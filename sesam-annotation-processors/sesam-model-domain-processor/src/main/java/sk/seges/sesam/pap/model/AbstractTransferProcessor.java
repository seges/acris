package sk.seges.sesam.pap.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.annotation.Id;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.configurer.TrasferObjectProcessorConfigurer;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationContext;
import sk.seges.sesam.pap.model.model.ConfigurationEnvironment;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.provider.ConfigurationCache;
import sk.seges.sesam.pap.model.provider.RoundEnvConfigurationProvider;
import sk.seges.sesam.pap.model.provider.TransferObjectProcessorContextProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.DefaultEntityResolver;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

public abstract class AbstractTransferProcessor extends MutableAnnotationProcessor {

	protected TransferObjectHelper toHelper;
	protected TransferObjectProcessorContextProvider transferObjectContextProvider;
	
	protected TransferObjectProcessingEnvironment processingEnv;
	protected EnvironmentContext<TransferObjectProcessingEnvironment> environmentContext;
	
	protected EntityResolver getEntityResolver() {
		return new DefaultEntityResolver();
	}

	protected ConfigurationCache getConfigurationCache() {
		return new ConfigurationCache();
	}
	
	protected EnvironmentContext<TransferObjectProcessingEnvironment> getEnvironmentContext() {
		if (environmentContext == null) {
			ConfigurationEnvironment configurationEnv = new ConfigurationEnvironment(processingEnv, roundEnv, getConfigurationCache());
			environmentContext = configurationEnv.getEnvironmentContext();
			configurationEnv.setConfigurationProviders(getConfigurationProviders());
		}
		
		return environmentContext;
	}
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new TrasferObjectProcessorConfigurer();
	}
	
	protected boolean isProcessed(List<String> generated, String fieldName) {
		return generated.contains(fieldName);
	}

	protected ConfigurationTypeElement getConfigurationElement(ProcessorContext context) {
		return getConfigurationTypeElement(context.getTypeElement());
	}

	protected ConfigurationTypeElement getConfigurationElement(RoundContext context) {
		return getConfigurationTypeElement(context.getTypeElement());
	}

	protected ConfigurationTypeElement getConfigurationTypeElement(TypeElement typeElement) {
		ConfigurationContext configurationContext = new ConfigurationContext(environmentContext.getConfigurationEnv());
		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(typeElement, getEnvironmentContext(), configurationContext);
		configurationContext.addConfiguration(configurationTypeElement);
		
		return configurationTypeElement;
	}
	
	@Override
	protected void processElement(ProcessorContext context) {
		TypeElement element = context.getTypeElement();
		
		ConfigurationTypeElement configurationElement = getConfigurationElement(context);

		if (configurationElement.getDomain() == null) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to find domain class reference for " + element.toString(), element);
			return;
		}

		MappingType mappingType = MappingType.AUTOMATIC;
		Mapping mapping =  element.getAnnotation(Mapping.class);

		if (mapping != null) {
			mappingType = mapping.value();
		}

		for (TransferObjectElementPrinter elementPrinter: getElementPrinters(context.getPrintWriter())) {
			elementPrinter.initialize(configurationElement, context.getOutputType());
			processMethods(configurationElement, mappingType, elementPrinter);
		}
	}

	protected abstract TransferObjectElementPrinter[] getElementPrinters(FormattedPrintWriter pw);
	
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		return getConfigurationElement(context).getDelegateConfigurationTypeElement() == null;
	}

	protected TransferObjectProcessorContextProvider getProcessorContextProvider(TransferObjectProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		return new TransferObjectProcessorContextProvider(getEnvironmentContext(), getEntityResolver());
	}

	@Override
	protected void init(Element element, RoundEnvironment roundEnv) {
		super.init(element, roundEnv);
		this.processingEnv = new TransferObjectProcessingEnvironment(super.processingEnv, roundEnv, getConfigurationCache());
		this.processingEnv.setConfigurationProviders(getConfigurationProviders());
		this.toHelper = new TransferObjectHelper(processingEnv);
		this.transferObjectContextProvider = getProcessorContextProvider(processingEnv, roundEnv);
	}
	
	protected ConfigurationProvider[] getConfigurationProviders() {
		return new ConfigurationProvider[] {
				new RoundEnvConfigurationProvider(getEnvironmentContext())
		};
	}

	protected void processMethods(ConfigurationTypeElement configurationTypeElement, MappingType mappingType, TransferObjectElementPrinter printer) {

		List<TransferObjectContext> contexts = new LinkedList<TransferObjectContext>();
		
		List<ExecutableElement> overridenMethods = ElementFilter.methodsIn(configurationTypeElement.asConfigurationElement().getEnclosedElements());
		
		DomainDeclaredType domainTypeElement = configurationTypeElement.getInstantiableDomain();
		DomainDeclaredType processingElement = domainTypeElement;
	
		List<String> generated = new ArrayList<String>();
		
		for (ExecutableElement overridenMethod: overridenMethods) {
			
			Ignore ignoreAnnotation = overridenMethod.getAnnotation(Ignore.class);
			if (ignoreAnnotation != null) {

				String fieldName = TransferObjectHelper.getFieldPath(overridenMethod);
				ExecutableElement domainMethod = processingElement.getGetterMethod(fieldName);

				if (getEntityResolver().isIdMethod(domainMethod)) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Id method can't be ignored. There should be an id method available for merging purposes.", configurationTypeElement.asConfigurationElement());
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
							" in the domain class " + domainTypeElement.getCanonicalName(), configurationTypeElement.asConfigurationElement());
					continue;
				}

				TransferObjectContext context = transferObjectContextProvider.get(configurationTypeElement, Modifier.PUBLIC, overridenMethod, domainMethod, getConfigurationProviders());
				if (context == null) {
					continue;
				}
				contexts.add(context);
				
				PathResolver pathResolver = new PathResolver(fieldName);
				String currentPath = pathResolver.next();
				String fullPath = currentPath;
				
				if (pathResolver.hasNext()) {

					DomainDeclaredType currentElement = processingElement;
	
					while (pathResolver.hasNext()) {
						DomainType domainReference = currentElement.getDomainReference(getEntityResolver(), currentPath);
						
						if (domainReference != null && domainReference.getKind().isDeclared()) {
							currentElement = (DomainDeclaredType)domainReference;
							
							ExecutableElement nestedIdMethod = currentElement.getIdMethod(getEntityResolver());

							if (nestedIdMethod == null && getEntityResolver().shouldHaveIdMethod(currentElement)) {
								//TODO Check @Id annotation is the configuration - nested field names
								processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method in the class " + currentElement.getCanonicalName() +
										". If the class/interface does not have strictly specified ID, please specify the id in the configuration using " + 
										Id.class.getCanonicalName() + " annotation.", configurationTypeElement.asConfigurationElement());
							} else {
								if (nestedIdMethod == null) {
									processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method in the class " + currentElement.getCanonicalName() + ".", 
											configurationTypeElement.asConfigurationElement());
								}
								//TODO Check if is not already generated
								context = transferObjectContextProvider.get(configurationTypeElement, Modifier.PROTECTED, nestedIdMethod, nestedIdMethod, fullPath, getConfigurationProviders());
								if (context == null) {
									continue;
								}

								contexts.add(context);
//								printer.print(context);
							}
						} else {
							if (pathResolver.hasNext()) {
								processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Invalid mapping specified in the field " + fieldName + ". Current path (" + 
										currentPath + ") address getter type that is not class/interfaces." +
										"You probably mistyped this field in the configuration.", configurationTypeElement.asConfigurationElement());
							}
						}

						currentPath = pathResolver.next();
						fullPath += "." + currentPath;
					}
				}
			}
		}

		processType(configurationTypeElement, mappingType, processingElement, domainTypeElement, generated, contexts);

		ExecutableElement idMethod = domainTypeElement.getIdMethod(getEntityResolver());
		
		if (idMethod == null && getEntityResolver().shouldHaveIdMethod(domainTypeElement)) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Identifier method could not be found in the automatic way. Please specify id method using " + 
					Id.class.getSimpleName() + " annotation or just specify id as a method name.", configurationTypeElement.asConfigurationElement());
			return;
		} else if (idMethod != null && !isProcessed(generated, MethodHelper.toField(idMethod))) {
			TransferObjectContext context = transferObjectContextProvider.get(configurationTypeElement, Modifier.PROTECTED, idMethod, idMethod, getConfigurationProviders());
			if (context != null) {
				contexts.add(context);
				//printer.print(context);
			}
		}

		Collections.sort(contexts, new Comparator<TransferObjectContext>() {

			@Override
			public int compare(TransferObjectContext o1, TransferObjectContext o2) {
				return o1.getDtoFieldName().compareTo(o2.getDtoFieldName());
			}
		});

		for (TransferObjectContext context: contexts) {
			printer.print(context);
		}
		
		printer.finish(configurationTypeElement);
	}
	
	private void takeSuperclassMethods(ConfigurationTypeElement configurationTypeElement, DomainDeclaredType processingElement, DomainDeclaredType domainTypeElement, MappingType mappingType, List<String> generated,
			List<TransferObjectContext> contexts) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(processingElement.asConfigurationElement().getEnclosedElements());
		
		if (mappingType.equals(MappingType.AUTOMATIC)) {
			for (ExecutableElement method: methods) {
				
				String fieldName = TransferObjectHelper.getFieldPath(method);
				
				boolean isProcessed = isProcessed(generated, fieldName);
				boolean isGetter = MethodHelper.isGetterMethod(method);
				boolean hasSetter = toHelper.hasSetterMethod(domainTypeElement.asConfigurationElement(), method);
				boolean isPublic = method.getModifiers().contains(Modifier.PUBLIC);
				if (!isProcessed && isGetter && hasSetter && isPublic) {

					generated.add(fieldName);
					
					if (processingElement.getDomainDefinitionConfiguration() == null) {
						TransferObjectContext context = transferObjectContextProvider.get(configurationTypeElement, Modifier.PUBLIC, method, method, getConfigurationProviders());
						if (context == null) {
							continue;
						}
						contexts.add(context);
					}
					
				} else if (!isProcessed && isGetter && !hasSetter && isPublic) {
					processingEnv.getMessager().printMessage(Kind.WARNING, "Method " + method.getSimpleName() + " does not have setter, type = " + processingElement.asConfigurationElement());
				}
			}
		}

		if (processingElement.getSuperClass() != null) {
			takeSuperclassMethods(configurationTypeElement, processingElement.getSuperClass(), domainTypeElement, mappingType, generated, contexts);
		}
	}
	
	private void processType(ConfigurationTypeElement configurationTypeElement, MappingType mappingType, final DomainDeclaredType processingElement, DomainDeclaredType domainTypeElement, List<String> generated, List<TransferObjectContext> contexts) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(processingElement.asConfigurationElement().getEnclosedElements());
		
		if (mappingType.equals(MappingType.AUTOMATIC)) {
			for (ExecutableElement method: methods) {
				
				String fieldName = TransferObjectHelper.getFieldPath(method);
				
				if (!isProcessed(generated, fieldName) && MethodHelper.isGetterMethod(method) && 
						toHelper.hasSetterMethod(domainTypeElement.asConfigurationElement(), method) && method.getModifiers().contains(Modifier.PUBLIC)) {

					generated.add(fieldName);

					TransferObjectContext context = transferObjectContextProvider.get(configurationTypeElement, Modifier.PUBLIC, method, method, getConfigurationProviders());
					if (context == null) {
						continue;
					}

					contexts.add(context);
				}
			}
		}
		
		if (processingElement.getSuperClass() == null && processingElement.asConfigurationElement() != null) {
			TypeMirror superclass = processingElement.asConfigurationElement().getSuperclass();
			if (superclass.getKind().equals(TypeKind.DECLARED)) {
				processType(configurationTypeElement, mappingType, (DomainDeclaredType) processingEnv.getTransferObjectUtils().getDomainType(superclass), domainTypeElement, generated, contexts);
			}
		} else if (processingElement.getSuperClass() != null) {
			takeSuperclassMethods(configurationTypeElement, processingElement.getSuperClass(), domainTypeElement, mappingType, generated, contexts);
		}

		for (TypeMirror domainInterface: processingElement.asConfigurationElement().getInterfaces()) {
			processType(configurationTypeElement, mappingType, (DomainDeclaredType) processingEnv.getTransferObjectUtils().getDomainType(domainInterface), domainTypeElement, generated, contexts);
		}
	}
	
}