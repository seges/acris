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
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;
import sk.seges.sesam.pap.model.model.ParameterFilter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.DefaultParametersResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.annotation.LocalService;
import sk.seges.sesam.pap.service.annotation.LocalServiceConverter;
import sk.seges.sesam.pap.service.utils.ServiceHelper;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceConverterProcessor extends AbstractConfigurableProcessor {

	public static final String SERVICE_CONVERTER_SUFFIX = "Exporter";
	private static final String SERVICE_DELEGATE_NAME = "service";

	private ServiceHelper serviceHelper;
	private ConverterProviderPrinter converterProviderPrinter;

	private Map<NamedType, Element> localInterfacesCache = new HashMap<NamedType, Element>();

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

	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {

		// type should be always declared type. If not, than strange things happen here
		TypeElement serviceElement = (TypeElement) ((DeclaredType) mutableType.asType()).asElement();

		Element[] localInterfaces = serviceHelper.getLocalServiceInterfaces(serviceElement);

		if (localInterfaces == null || localInterfaces.length == 0) {
			processingEnv.getMessager().printMessage(Kind.ERROR, 
					"[ERROR] Unable to find local interface for the service " + serviceElement
							+ ". You should specify local service interface using " + LocalService.class.getCanonicalName() + " annotation.", serviceElement);
			return null;
		}

		List<NamedType> result = new ArrayList<NamedType>();

		for (Element localInterface : localInterfaces) {
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
			Element[] localInterfaces = serviceHelper.getLocalServiceInterfaces(serviceElement);

			if (localInterfaces != null && localInterfaces.length > 0) {
				List<NamedType> result = new ArrayList<NamedType>();

				for (Element localInterface : localInterfaces) {
					result.add(nameTypesUtils.toType(serviceHelper.getRemoteServiceInterface(localInterface)));
				}

				return result.toArray(new Type[] {});
			}
			
			break;
		}
		
		return super.getOutputDefinition(type, serviceElement);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		this.serviceHelper = new ServiceHelper();
		return super.process(annotations, roundEnv);
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
	protected void writeClassAnnotations(Element element, NamedType outputClass, PrintWriter pw) {

		Element localInterface = localInterfacesCache.get(outputClass);
		if (localInterface == null) {
			super.writeClassAnnotations(element, outputClass, pw);
			return;
		}
		
		TypeElement remoteServiceInterface = (TypeElement) serviceHelper.getRemoteServiceInterface(localInterface);
		if (remoteServiceInterface == null) {
			super.writeClassAnnotations(element, outputClass, pw);
			return;
		}
		
		pw.println("@" + LocalServiceConverter.class.getCanonicalName() + "(remoteService = " + remoteServiceInterface.toString() + ".class)");
		super.writeClassAnnotations(element, outputClass, pw);
	}
	
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, PrintWriter pw) {

		this.converterProviderPrinter = new ConverterProviderPrinter(pw, processingEnv, getParametersResolver());

		Element localInterface = localInterfacesCache.get(outputClass);

		if (localInterface == null) {
			processingEnv.getMessager().printMessage(
					Kind.ERROR,
					"[ERROR] Unable to find local interface for the service " + element
							+ ". Most probably you found a bug in the sesam, report this bug immediately :-).", element);
			return;
		}

		pw.println("private " + localInterface.toString() + " " + SERVICE_DELEGATE_NAME + ";");

		List<ConverterParameter> converterParameters = unifyParameterNames(getConverterParameters(element, localInterface));
		List<ConverterParameter> mergedSameParameters = mergeSameParams(converterParameters);

		for (ConverterParameter converterParameter : mergedSameParameters) {
			pw.println("private " + converterParameter.getType().toString() + " " + converterParameter.getName() + ";");
			pw.println();
		}

		pw.println();
		pw.print("public " + outputClass.getSimpleName() + "(" + localInterface.toString() + " " + SERVICE_DELEGATE_NAME);

		for (ConverterParameter converterParameter : mergedSameParameters) {
			pw.print(", " + converterParameter.getType().toString() + " " + converterParameter.getName());
		}

		pw.println(") {");
		pw.println("this." + SERVICE_DELEGATE_NAME + " = " + SERVICE_DELEGATE_NAME + ";");

		for (ConverterParameter converterParameter : mergedSameParameters) {
			pw.println("this." + converterParameter.getName() + " = " + converterParameter.getName() + ";");
		}

		pw.println("}");
		pw.println();

		copyMethods(element, localInterface, converterParameters, pw);
	}

	protected ParametersResolver getParametersResolver() {
		return new DefaultParametersResolver(processingEnv);
	}
	
	protected List<ConverterParameter> getConverterParameters(Element element, Element localInterface) {
		TypeElement remoteServiceInterface = (TypeElement) serviceHelper.getRemoteServiceInterface(localInterface);

		List<ConverterParameter> parameters = new LinkedList<ConverterParameter>();

		if (remoteServiceInterface == null) {
			return parameters;
		}

		List<ExecutableElement> remoteMethods = ElementFilter.methodsIn(remoteServiceInterface.getEnclosedElements());

		Set<ConverterTypeElement> converters = new HashSet<ConverterTypeElement>();

		for (ExecutableElement remoteMethod : remoteMethods) {
			ExecutableElement localMethod = getDomainMethodPair(remoteMethod, element, remoteServiceInterface);
			if (localMethod == null) {
				continue;
			}

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				
				DtoTypeElement dtoReturnType = new DtoTypeElement(remoteServiceInterface, remoteMethod.getReturnType(), processingEnv, roundEnv);

				ConverterTypeElement converter = dtoReturnType.getConverter();
			
				if (!converters.contains(converter)) {
					parameters.addAll(converter.getConverterParameters(getParametersResolver()));
					converters.add(converter);
				}
			}

			for (int index = 0; index < localMethod.getParameters().size(); index++) {
				TypeMirror dtoType = remoteMethod.getParameters().get(index).asType();

				DtoTypeElement dtoReturnType = new DtoTypeElement(remoteServiceInterface, dtoType, processingEnv, roundEnv);

				ConverterTypeElement converter = dtoReturnType.getConverter();

				if (!converters.contains(converter)) {
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
	
	protected void copyMethods(Element element, Element localInterface, List<ConverterParameter> parameters, PrintWriter pw) {

		TypeElement remoteServiceInterface = (TypeElement) serviceHelper.getRemoteServiceInterface(localInterface);

		if (remoteServiceInterface == null) {
			processingEnv.getMessager().printMessage(Kind.ERROR,
					"[ERROR] Unable to find remote service pair for the local service definition " + localInterface.toString(), element);
			return;
		}

		List<ExecutableElement> remoteMethods = ElementFilter.methodsIn(remoteServiceInterface.getEnclosedElements());

		Map<DtoTypeElement, String> dtoConverters = new HashMap<DtoTypeElement, String>();
		Map<DomainTypeElement, String> domainConverters = new HashMap<DomainTypeElement, String>();

		for (ExecutableElement remoteMethod : remoteMethods) {
			ExecutableElement localMethod = getDomainMethodPair(remoteMethod, element, remoteServiceInterface);
			if (localMethod == null) {
				processingEnv.getMessager().printMessage(
						Kind.ERROR,
						"[ERROR] Service class does not implements local service method " + remoteMethod.toString()
								+ ". Please specify correct service implementation.", element);
				continue;
			}


			DtoTypeElement returnDtoType = null;
			
			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				returnDtoType = new DtoTypeElement(remoteServiceInterface, remoteMethod.getReturnType(), processingEnv, roundEnv);
				
				if (remoteMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
					returnDtoType = new DtoTypeElement(remoteServiceInterface, remoteMethod.getReturnType(), processingEnv, roundEnv);
				}

				if (!dtoConverters.containsKey(returnDtoType)) {
					String convertToDtoMethodName = converterProviderPrinter.getConverterMethodName(returnDtoType.getConverter());
					dtoConverters.put(returnDtoType, convertToDtoMethodName);
				}
			}

			for (int index = 0; index < localMethod.getParameters().size(); index++) {
				TypeMirror dtoType = remoteMethod.getParameters().get(index).asType();
				returnDtoType = new DtoTypeElement(remoteServiceInterface, dtoType, processingEnv, roundEnv);
				DomainTypeElement parameterDomainType = returnDtoType.getDomainTypeElement();

				if (!domainConverters.containsKey(parameterDomainType)) {
					String convertFromDtoMethodName = converterProviderPrinter.getConverterMethodName(returnDtoType.getConverter());
					domainConverters.put(parameterDomainType, convertFromDtoMethodName);
				}
			}

			methodHelper.copyAnnotations(localMethod, pw);
			methodHelper.copyMethodDefinition(remoteMethod, ClassSerializer.CANONICAL, pw);
			pw.println("{");

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				if (dtoConverters.get(returnDtoType) == null) {
					pw.print("return ");
				} else {
					
					String converterName = dtoConverters.get(returnDtoType);
					
					TypeMirror dtoType = remoteMethod.getReturnType();
					
					String returnType = getNameTypes().toType(dtoType).toString(ClassSerializer.CANONICAL, true);

					pw.print("return (" + returnType + ")" + converterName + "().toDto(");
				}
			}

			pw.print(SERVICE_DELEGATE_NAME + "." + localMethod.getSimpleName().toString() + "(");

			for (int i = 0; i < localMethod.getParameters().size(); i++) {
				if (i > 0) {
					pw.print(", ");
				}

				TypeMirror dtoType = remoteMethod.getParameters().get(i).asType();
				
				DtoTypeElement parameterDtoType = new DtoTypeElement(remoteServiceInterface, dtoType, processingEnv, roundEnv);
				DomainTypeElement parameterDomainType = parameterDtoType.getDomainTypeElement();
				
				String domainConverter = domainConverters.get(parameterDomainType);
				
				String returnType = null;
				
				if (domainConverter != null) {
					returnType = parameterDomainType.toString(ClassSerializer.CANONICAL, true);
				}
				
				if (domainConverter != null) {
					pw.print("(" + returnType + ")" + domainConverter + "().fromDto(");
				}

				pw.print(remoteMethod.getParameters().get(i).getSimpleName().toString());

				if (domainConverter != null) {
					pw.print(")");
				}
			}

			pw.print(")");

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID) && dtoConverters.get(returnDtoType) != null) {
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

	protected ExecutableElement getDomainMethodPair(ExecutableElement remoteMethod, Element serviceElement, TypeElement remoteServiceElement) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(serviceElement.getEnclosedElements());

		for (ExecutableElement method : methods) {
			boolean pairMethod = false;

			if (method.getSimpleName().toString().equals(remoteMethod.getSimpleName().toString())
					&& method.getParameters().size() == remoteMethod.getParameters().size()) {
				pairMethod = true;
				int index = 0;
				for (VariableElement dtoParameter : remoteMethod.getParameters()) {
					
					DtoTypeElement parameterDtoType = new DtoTypeElement(remoteServiceElement, dtoParameter.asType(), processingEnv, roundEnv);
					DomainTypeElement parameterDomainType = parameterDtoType.getDomainTypeElement();

					if (!getNameTypes().toType(method.getParameters().get(index).asType()).equals(parameterDomainType)) {
						pairMethod = false;
						break;
					}
					index++;
				}
			}

			if (pairMethod) {
				
				DtoTypeElement returnDtoType = new DtoTypeElement(remoteServiceElement, method.getReturnType(), processingEnv, roundEnv);
				DomainTypeElement returnDomainType = returnDtoType.getDomainTypeElement();

				if (getNameTypes().toType(method.getReturnType()).equals(returnDomainType)) {
					return method;
				}
				processingEnv.getMessager().printMessage(
						Kind.ERROR,
						"[ERROR] Service method return type does not match the remote interface definition " + remoteMethod.toString()
								+ ". This should have never happened, you are probably a magician or there is a bug in the sesam processor itself.",
						serviceElement);
			}
		}

		return null;
	}
}