package sk.seges.sesam.pap.service;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;
import sk.seges.sesam.pap.model.model.ParameterFilter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.DefaultParametersResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.annotation.LocalService;
import sk.seges.sesam.pap.service.annotation.LocalServiceConverter;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.provider.ServiceCollectorConfigurationProvider;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceConverterProcessor extends AbstractConfigurableProcessor {

	public static final String SERVICE_CONVERTER_SUFFIX = "Exporter";
	private static final String SERVICE_DELEGATE_NAME = "service";

	private ConverterProviderPrinter converterProviderPrinter;

	private ConfigurationProvider[] configurationProviders;
	
	private Map<NamedType, LocalServiceTypeElement> localInterfacesCache = new HashMap<NamedType, LocalServiceTypeElement>();

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ServiceConverterProcessorConfiguration();
	}

	public static NamedType getOutputClass(ImmutableType mutableType, PackageValidatorProvider packageValidatorProvider) {
		return mutableType.addClassSufix(SERVICE_CONVERTER_SUFFIX);
	}

	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	protected ConfigurationProvider[] getConfigurationProviders(ServiceTypeElement service) {
		return new ConfigurationProvider[] {
				new ServiceCollectorConfigurationProvider(service, processingEnv, roundEnv)
		};
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {

		// type should be always declared type. If not, than strange things happen here
		TypeElement serviceElement = (TypeElement) ((DeclaredType) mutableType.asType()).asElement();

		ServiceTypeElement serviceTypeElement = new ServiceTypeElement(serviceElement, processingEnv);
		List<LocalServiceTypeElement> localServiceInterfaces = serviceTypeElement.getLocalServiceInterfaces();
		
		if (localServiceInterfaces == null || localServiceInterfaces.size() == 0) {
			processingEnv.getMessager().printMessage(Kind.ERROR, 
					"[ERROR] Unable to find local interface for the service " + serviceElement
							+ ". You should specify local service interface using " + LocalService.class.getCanonicalName() + " annotation.", serviceElement);
			return new NamedType[] {};
		}

		List<NamedType> result = new ArrayList<NamedType>();

		for (LocalServiceTypeElement localInterface : localServiceInterfaces) {
			NamedType outputClass = getOutputClass(getNameTypes().toImmutableType(localInterface), getPackageValidatorProvider());
			localInterfacesCache.put(outputClass, localInterface);
			result.add(outputClass);
		}

		return result.toArray(new NamedType[] {});
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement serviceElement) {
		switch (type) {
		case OUTPUT_INTERFACES:
			
			List<LocalServiceTypeElement> localServiceInterfaces = new ServiceTypeElement(serviceElement, processingEnv).getLocalServiceInterfaces();

			if (localServiceInterfaces != null && localServiceInterfaces.size() > 0) {
				List<NamedType> result = new ArrayList<NamedType>();

				for (LocalServiceTypeElement localInterface : localServiceInterfaces) {
					result.add(localInterface.getRemoteServiceInterface());
				}

				return result.toArray(new Type[] {});
			}
			
			break;
		}
		
		return super.getOutputDefinition(type, serviceElement);
	}

	// TODO If the converter has 2 same parameters, like Cache cache1, Cache cache2
	// and another converter has same 2 parameters, Cache cache1, Cache cache2, so the result won't be only unify cache parameter but 2, cache1 and
	// cache2
	private ConverterParameter getSameByType(NamedType typeParameter, Collection<ConverterParameter> parameters) {
		for (ConverterParameter parameter : parameters) {
			if (parameter.getType().equals(typeParameter)) {
				return parameter;
			}
		}

		return null;
	}

	private List<ConverterParameter> mergeSameParams(List<ConverterParameter> converterParameters) {
		List<ConverterParameter> result = new LinkedList<ConverterParameter>();

		for (ConverterParameter parameter : converterParameters) {
			ConverterParameter sameParameterByType = getSameByType(parameter.getType(), result);
			if (sameParameterByType == null) {
				result.add(parameter);
			} else {
				parameter.setSameParameter(sameParameterByType);
			}
		}

		return result;
	}

	protected List<ConverterParameter> removeConverterParameters(List<ConverterParameter> parameters) {
		List<ConverterParameter> result = new ArrayList<ConverterParameter>();
		for (ConverterParameter parameter: parameters) {
 			if (!ProcessorUtils.implementsType(nameTypesUtils.fromType(parameter.getType()), processingEnv.getElementUtils().getTypeElement(DtoConverter.class.getCanonicalName()).asType()))  {
 				result.add(parameter);
 			}
		}
		return result;
	}
	
	protected List<ConverterParameter> unifyParameterNames(List<ConverterParameter> parameters) {
		List<ConverterParameter> result = new LinkedList<ConverterParameter>();

		for (ConverterParameter parameter : parameters) {
			int index = 1;
			String name = parameter.getName();
			while (ParameterFilter.NAME.filterBy(result, parameter).size() > 0) {
				parameter.setName(name + index++);
			}
			result.add(parameter);
		}

		return result;
	}

	@Override
	protected void writeClassAnnotations(Element element, NamedType outputClass, FormattedPrintWriter pw) {

		LocalServiceTypeElement localInterface = localInterfacesCache.get(outputClass);
		if (localInterface == null) {
			super.writeClassAnnotations(element, outputClass, pw);
			return;
		}
		
		RemoteServiceTypeElement remoteServiceInterface = localInterface.getRemoteServiceInterface();
		if (remoteServiceInterface == null) {
			super.writeClassAnnotations(element, outputClass, pw);
			return;
		}
		
		//TODO if one service handles more remote services ?
		pw.println("@", LocalServiceConverter.class, "(remoteService = ", remoteServiceInterface, ".class)");
		super.writeClassAnnotations(element, outputClass, (PrintWriter) pw);
	}
	
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, FormattedPrintWriter pw) {

		ServiceTypeElement serviceTypeElement = new ServiceTypeElement(element, processingEnv);
		this.configurationProviders = getConfigurationProviders(serviceTypeElement);
		this.converterProviderPrinter = new ConverterProviderPrinter(pw, processingEnv, roundEnv, getParametersResolver(), configurationProviders);
		
		LocalServiceTypeElement localInterface = localInterfacesCache.get(outputClass);

		if (localInterface == null) {
			processingEnv.getMessager().printMessage(Kind.ERROR,
					"[ERROR] Unable to find local interface for the service " + element
							+ ". Most probably you found a bug in the sesam, report this bug immediately :-).", element);
			return;
		}

		pw.println("private ", localInterface, " " + SERVICE_DELEGATE_NAME + ";");

		List<ConverterParameter> converterParameters = unifyParameterNames(removeConverterParameters(getConverterParameters(serviceTypeElement, localInterface)));
		List<ConverterParameter> mergedSameParameters = mergeSameParams(converterParameters);

		for (ConverterParameter converterParameter : mergedSameParameters) {
			pw.println("private ", converterParameter.getType(), " " + converterParameter.getName() + ";");
			pw.println();
		}

		pw.println();
		pw.print("public " + outputClass.getSimpleName() + "(", localInterface, " " + SERVICE_DELEGATE_NAME);

		for (ConverterParameter converterParameter : mergedSameParameters) {
			pw.print(", ", converterParameter.getType(), " " + converterParameter.getName());
		}

		pw.println(") {");
		pw.println("this." + SERVICE_DELEGATE_NAME + " = " + SERVICE_DELEGATE_NAME + ";");

		for (ConverterParameter converterParameter : mergedSameParameters) {
			pw.println("this." + converterParameter.getName() + " = " + converterParameter.getName() + ";");
		}

		pw.println("}");
		pw.println();

		copyMethods(serviceTypeElement, localInterface, converterParameters, pw);
		
		this.converterProviderPrinter.printConverterMethods(true);
	}

	protected ParametersResolver getParametersResolver() {
		return new DefaultParametersResolver(processingEnv);
	}
	
	/**
	 * Collects all converters from the remote service interface methods return types and method parameters and returns all parameters
	 * required by these converters.
	 * This parameters should be passed to the service exporter constructor and initialized by dependency injection of in the upper layer.
	 */
	protected List<ConverterParameter> getConverterParameters(ServiceTypeElement serviceTypeElement, LocalServiceTypeElement localInterface) {
		RemoteServiceTypeElement remoteServiceInterface = localInterface.getRemoteServiceInterface();

		List<ConverterParameter> parameters = new LinkedList<ConverterParameter>();

		if (remoteServiceInterface == null) {
			return parameters;
		}

		List<ExecutableElement> remoteMethods = ElementFilter.methodsIn(remoteServiceInterface.asElement().getEnclosedElements());

		Set<ConverterTypeElement> converters = new HashSet<ConverterTypeElement>();

		for (ExecutableElement remoteMethod : remoteMethods) {
			ExecutableElement localMethod = getDomainMethodPair(remoteMethod, serviceTypeElement);
			
			//If the remote method does not have local method pair then invalid service interface are used
			if (localMethod == null) {
				continue;
			}

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				
				DtoTypeElement dtoReturnType = new DtoTypeElement(remoteMethod.getReturnType(), processingEnv, roundEnv, configurationProviders);

				ConverterTypeElement converter = dtoReturnType.getConverter();
			
				if (converter != null && !converters.contains(converter)) {
					parameters.addAll(converter.getConverterParameters(getParametersResolver()));
					converters.add(converter);
				}
			}

			for (int index = 0; index < localMethod.getParameters().size(); index++) {
				TypeMirror dtoType = remoteMethod.getParameters().get(index).asType();

				DtoTypeElement dtoReturnType = new DtoTypeElement(dtoType, processingEnv, roundEnv, configurationProviders);

				ConverterTypeElement converter = dtoReturnType.getConverter();

				if (converter != null && !converters.contains(converter)) {
					parameters.addAll(converter.getConverterParameters(getParametersResolver()));
					converters.add(converter);
				}
			}
		}

		return parameters;
	}

	protected List<ConverterParameter> getConverterParameters(ConverterTypeElement converter, List<ConverterParameter> parameters) {
		ConverterParameter dummyParameter = new ConverterParameter();
		dummyParameter.setConverter(converter);
		return ParameterFilter.CONVERTER.filterBy(parameters, dummyParameter);
	}
	
	protected void copyMethods(ServiceTypeElement serviceTypeElement, LocalServiceTypeElement localInterface, List<ConverterParameter> parameters, FormattedPrintWriter pw) {

		RemoteServiceTypeElement remoteServiceInterface = localInterface.getRemoteServiceInterface();

		if (remoteServiceInterface == null) {
			processingEnv.getMessager().printMessage(Kind.ERROR,
					"[ERROR] Unable to find remote service pair for the local service definition " + localInterface.toString(), serviceTypeElement.asElement());
			return;
		}

		List<ExecutableElement> remoteMethods = ElementFilter.methodsIn(remoteServiceInterface.asElement().getEnclosedElements());

		for (ExecutableElement remoteMethod : remoteMethods) {
			ExecutableElement localMethod = getDomainMethodPair(remoteMethod, serviceTypeElement);
			if (localMethod == null) {
				processingEnv.getMessager().printMessage(Kind.ERROR,
						"[ERROR] Service class does not implements local service method " + remoteMethod.toString()
								+ ". Please specify correct service implementation.", serviceTypeElement.asElement());
				continue;
			}


			DtoTypeElement returnDtoType = null;
			
			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				returnDtoType = new DtoTypeElement(remoteMethod.getReturnType(), processingEnv, roundEnv, configurationProviders);
			}

			methodHelper.copyAnnotations(localMethod, pw);
			methodHelper.copyMethodDefinition(remoteMethod, pw);
			pw.println("{");

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				
				if (returnDtoType.getConverter() == null) {
					pw.print("return ");
				} else {
					pw.print("return (", getNameTypes().toType(remoteMethod.getReturnType()), ")");
					converterProviderPrinter.printDtoConverterMethodName(returnDtoType.getConverter(), nameTypesUtils.fromType(returnDtoType), pw);
					pw.print(".toDto(");
				}
			}

			pw.print(SERVICE_DELEGATE_NAME + "." + localMethod.getSimpleName().toString() + "(");

			for (int i = 0; i < localMethod.getParameters().size(); i++) {
				if (i > 0) {
					pw.print(", ");
				}

				TypeMirror dtoType = remoteMethod.getParameters().get(i).asType();
				
				DtoTypeElement parameterDtoType = new DtoTypeElement(dtoType, processingEnv, roundEnv, configurationProviders);
				DomainTypeElement parameterDomainType = parameterDtoType.getDomainTypeElement();
				
				if (parameterDtoType.getConverter() != null) {
					pw.print("(", parameterDomainType, ")");
					converterProviderPrinter.printDtoConverterMethodName(parameterDtoType.getConverter(), nameTypesUtils.fromType(parameterDtoType), pw);
					pw.print(".fromDto(");
				}

				pw.print(remoteMethod.getParameters().get(i).getSimpleName().toString());

				if (parameterDtoType.getConverter() != null) {
					pw.print(")");
				}
			}

			pw.print(")");

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID) && returnDtoType.getConverter() != null) {
				pw.print(")");
			}
			pw.println(";");
			pw.println("}");
			pw.println();
		}
	}

	protected String getConverterMethodName(NamedType domainClass) {
		return "getConverter";
	}

	protected String getParameterName(ConverterParameter parameter) {
		if (parameter.getSameParameter() != null) {
			return parameter.getSameParameter().getName();
		}

		return parameter.getName();
	}

	protected ExecutableElement getDomainMethodPair(ExecutableElement remoteMethod, ServiceTypeElement serviceTypeElement) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(serviceTypeElement.asElement().getEnclosedElements());

		for (ExecutableElement method : methods) {
			boolean pairMethod = false;

			if (method.getSimpleName().toString().equals(remoteMethod.getSimpleName().toString())
					&& method.getParameters().size() == remoteMethod.getParameters().size()) {
				pairMethod = true;
				int index = 0;
				for (VariableElement dtoParameter : remoteMethod.getParameters()) {
					
					DtoTypeElement parameterDtoType = new DtoTypeElement(dtoParameter.asType(), processingEnv, roundEnv, configurationProviders);
					DomainTypeElement parameterDomainType = parameterDtoType.getDomainTypeElement();

					if (!parameterDomainType.equals(getNameTypes().toType(method.getParameters().get(index).asType()))) {
						pairMethod = false;
						break;
					}
					index++;
				}
			}

			if (pairMethod) {
				
				DtoTypeElement returnDtoType = new DtoTypeElement(method.getReturnType(), processingEnv, roundEnv, configurationProviders);
				DomainTypeElement returnDomainType = returnDtoType.getDomainTypeElement();

				if (returnDomainType.equals(getNameTypes().toType(method.getReturnType()))) {
					return method;
				}
				processingEnv.getMessager().printMessage(Kind.ERROR,
						"[ERROR] Service method return type does not match the remote interface definition " + remoteMethod.toString()
								+ ". This should have never happened, you are probably a magician or there is a bug in the sesam processor itself.",
						serviceTypeElement.asElement());
			}
		}

		return null;
	}
}