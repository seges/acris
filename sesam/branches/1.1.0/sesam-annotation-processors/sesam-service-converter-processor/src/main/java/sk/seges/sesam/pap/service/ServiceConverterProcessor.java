package sk.seges.sesam.pap.service;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.pap.model.utils.TransferObjectConfiguration.DtoMappingType;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;
import sk.seges.sesam.pap.service.annotation.LocalService;
import sk.seges.sesam.pap.service.annotation.LocalServiceConverter;
import sk.seges.sesam.pap.service.utils.ServiceHelper;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceConverterProcessor extends AbstractConfigurableProcessor {

	class ConverterParameter {

		private String name;
		private TypeMirror type;
		private NamedType converter;
		private ConverterParameter sameParameter;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public TypeMirror getType() {
			return type;
		}

		public void setType(TypeMirror typeMirror) {
			this.type = typeMirror;
		}

		public NamedType getConverter() {
			return converter;
		}

		public void setConverter(NamedType converter) {
			this.converter = converter;
		}

		public ConverterParameter getSameParameter() {
			return sameParameter;
		}

		public void setSameParameter(ConverterParameter sameParameter) {
			this.sameParameter = sameParameter;
		}
	}

	enum ParameterFilter {
		NAME {
			@Override
			protected boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2) {
				return parameter1.getName().equals(parameter2.getName());
			}
		},
		TYPE {
			@Override
			protected boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2) {
				return parameter1.getType().equals(parameter2.getType());
			}
		},
		CONVERTER {
			@Override
			protected boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2) {
				return parameter1.getConverter().equals(parameter2.getConverter());
			}
		};

		public Set<ConverterParameter> filterBy(Set<ConverterParameter> parameters, ConverterParameter parameter) {
			Set<ConverterParameter> result = new HashSet<ConverterParameter>();
			for (ConverterParameter converterParameter : parameters) {
				if (equalsBy(converterParameter, parameter)) {
					result.add(converterParameter);
				}
			}

			return result;
		}

		protected abstract boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2);
	}

	public static final String SERVICE_CONVERTER_SUFFIX = "Exporter";
	private static final String SERVICE_DELEGATE_NAME = "service";

	private TransferObjectHelper toHelper;
	private ServiceHelper serviceHelper;

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
			processingEnv.getMessager().printMessage(
					Kind.ERROR,
					"[ERROR] Unable to find local interface for the service " + serviceElement
							+ ". You should specify local service interface using " + LocalService.class.getCanonicalName() + " annotation.",
					serviceElement);
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
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		this.toHelper = new TransferObjectHelper(getNameTypes(), processingEnv, roundEnv);
		this.serviceHelper = new ServiceHelper();
		return super.process(annotations, roundEnv);
	}

	// TODO If the converter has 2 same parameters, like Cache cache1, Cache cache2
	// and another converter has same 2 parameters, Cache cache1, Cache cache2, so the result won't be only unify cache parameter but 2, cache1 and
	// cache2
	private ConverterParameter getSameByType(TypeMirror typeParameter, Set<ConverterParameter> parameters) {
		for (ConverterParameter parameter : parameters) {
			if (parameter.getType().equals(typeParameter)) {
				return parameter;
			}
		}

		return null;
	}

	private Set<ConverterParameter> mergeSameParams(Set<ConverterParameter> converterParameters) {
		Set<ConverterParameter> result = new HashSet<ConverterParameter>();

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

	protected Set<ConverterParameter> unifyParameterNames(Set<ConverterParameter> parameters) {
		Set<ConverterParameter> result = new HashSet<ConverterParameter>();

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

		Element localInterface = localInterfacesCache.get(outputClass);

		if (localInterface == null) {
			processingEnv.getMessager().printMessage(
					Kind.ERROR,
					"[ERROR] Unable to find local interface for the service " + element
							+ ". Most probably you found a bug in the sesam, report this bug immediately. Or not :-).", element);
			return;
		}

		pw.println("private " + localInterface.toString() + " " + SERVICE_DELEGATE_NAME + ";");

		Set<ConverterParameter> converterParameters = unifyParameterNames(getConverterParameters(element, localInterface));
		Set<ConverterParameter> mergedSameParameters = mergeSameParams(converterParameters);

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

	protected Set<ConverterParameter> getConverterParameters(Element element, Element localInterface) {
		TypeElement remoteServiceInterface = (TypeElement) serviceHelper.getRemoteServiceInterface(localInterface);

		Set<ConverterParameter> parameters = new HashSet<ConverterParameter>();

		if (remoteServiceInterface == null) {
			return parameters;
		}

		List<ExecutableElement> remoteMethods = ElementFilter.methodsIn(remoteServiceInterface.getEnclosedElements());

		Set<NamedType> converters = new HashSet<NamedType>();

		for (ExecutableElement remoteMethod : remoteMethods) {
			ExecutableElement localMethod = getDomainMethodPair(remoteMethod, element, remoteServiceInterface);
			if (localMethod == null) {
				continue;
			}

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				
				NamedType converter = toHelper.getDtoMappingClass(remoteMethod.getReturnType(), remoteServiceInterface, DtoMappingType.CONVERTER);
			
				if (!converters.contains(converter)) {
					parameters.addAll(getConverterParameters(converter));
					converters.add(converter);
				}
			}

			for (int index = 0; index < localMethod.getParameters().size(); index++) {
				TypeMirror dtoType = remoteMethod.getParameters().get(index).asType();
				NamedType converter = toHelper.getDtoMappingClass(dtoType, remoteServiceInterface, DtoMappingType.CONVERTER);
				if (!converters.contains(converter)) {
					parameters.addAll(getConverterParameters(converter));
					converters.add(converter);
				}
			}
		}

		return parameters;
	}

	protected Set<ConverterParameter> getConverterParameters(NamedType converter, Set<ConverterParameter> parameters) {
		ConverterParameter dummyParameter = new ConverterParameter();
		dummyParameter.setConverter(converter);
		return ParameterFilter.CONVERTER.filterBy(parameters, dummyParameter);
	}

	protected Set<ConverterParameter> getConverterParameters(NamedType converter) {

		Set<ConverterParameter> parameters = new HashSet<ConverterParameter>();

		if (converter != null) {
			TypeElement converterTypeElement = processingEnv.getElementUtils().getTypeElement(converter.getCanonicalName());
			List<ExecutableElement> constructors = ElementFilter.constructorsIn(converterTypeElement.getEnclosedElements());

			if (constructors != null && constructors.size() > 0) {
				List<? extends VariableElement> constructorParameters = constructors.get(0).getParameters();

				for (VariableElement constructorParameter : constructorParameters) {
					ConverterParameter converterParameter = new ConverterParameter();
					converterParameter.setType(constructorParameter.asType());
					converterParameter.setName(constructorParameter.getSimpleName().toString());
					converterParameter.setConverter(converter);
					parameters.add(converterParameter);
				}
			}
		}

		return parameters;
	}

	protected void copyMethods(Element element, Element localInterface, Set<ConverterParameter> parameters, PrintWriter pw) {

		TypeElement remoteServiceInterface = (TypeElement) serviceHelper.getRemoteServiceInterface(localInterface);

		if (remoteServiceInterface == null) {
			processingEnv.getMessager().printMessage(Kind.ERROR,
					"[ERROR] Unable to find remote service pair for the local service definition " + localInterface.toString(), element);
			return;
		}

		List<ExecutableElement> remoteMethods = ElementFilter.methodsIn(remoteServiceInterface.getEnclosedElements());

		Map<TypeMirror, String> dtoConverters = new HashMap<TypeMirror, String>();
		Map<TypeMirror, String> domainConverters = new HashMap<TypeMirror, String>();

		for (ExecutableElement remoteMethod : remoteMethods) {
			ExecutableElement localMethod = getDomainMethodPair(remoteMethod, element, remoteServiceInterface);
			if (localMethod == null) {
				processingEnv.getMessager().printMessage(
						Kind.ERROR,
						"[ERROR] Service class does not implements local service method " + remoteMethod.toString()
								+ ". Please specify correct service implementation.", element);
				continue;
			}

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID) && !dtoConverters.containsKey(remoteMethod.getReturnType())) {
				String convertToDtoMethodName = printToDtoConvertMethod(remoteMethod.getReturnType(), remoteServiceInterface, parameters, pw);
				dtoConverters.put(remoteMethod.getReturnType(), convertToDtoMethodName);
			}

			for (int index = 0; index < localMethod.getParameters().size(); index++) {
				TypeMirror dtoType = remoteMethod.getParameters().get(index).asType();
				if (!domainConverters.containsKey(dtoType)) {
					String convertFromDtoMethodName = printFromDtoConvertMethod(dtoType, remoteServiceInterface, parameters, pw);
					domainConverters.put(dtoType, convertFromDtoMethodName);
				}
			}

			methodHelper.copyAnnotations(localMethod, pw);
			methodHelper.copyMethodDefinition(remoteMethod, ClassSerializer.CANONICAL, pw);
			pw.println("{");

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				if (dtoConverters.get(remoteMethod.getReturnType()) == null) {
					pw.print("return ");
				} else {
					pw.print("return " + dtoConverters.get(remoteMethod.getReturnType()) + "(");
				}
			}

			pw.print(SERVICE_DELEGATE_NAME + "." + localMethod.getSimpleName().toString() + "(");

			for (int i = 0; i < localMethod.getParameters().size(); i++) {
				if (i > 0) {
					pw.print(", ");
				}

				TypeMirror dtoType = remoteMethod.getParameters().get(i).asType();

				String domainConverter = domainConverters.get(dtoType);

				if (domainConverter != null) {
					pw.print(domainConverter + "(");
				}

				pw.print(remoteMethod.getParameters().get(i).getSimpleName().toString());

				if (domainConverter != null) {
					pw.print(")");
				}
			}

			pw.print(")");

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID) && dtoConverters.get(remoteMethod.getReturnType()) != null) {
				pw.print(")");
			}
			pw.println(";");
			pw.println("}");
			pw.println();
		}
	}

	protected String getToDtoConverterMethodName(NamedType domainClass) {
		return "convertToDto";
	}

	protected String getFromDtoConverterMethodName(NamedType domainClass) {
		return "convertFromDto";
	}

	protected String getParameterName(ConverterParameter parameter) {
		if (parameter.getSameParameter() != null) {
			return parameter.getSameParameter().getName();
		}

		return parameter.getName();
	}

	protected String printFromDtoConvertMethod(TypeMirror dtoType, TypeElement remoteServiceElement, Set<ConverterParameter> parameters,
			PrintWriter pw) {
		NamedType converter = toHelper.getDtoMappingClass(dtoType, remoteServiceElement, DtoMappingType.CONVERTER);

		if (converter == null) {
			return null;
		}

		NamedType domainClass = toHelper.getDtoMappingClass(dtoType, remoteServiceElement, DtoMappingType.DOMAIN);

		String convertMethod = getFromDtoConverterMethodName(domainClass);
		String returnType = domainClass.toString(null, ClassSerializer.CANONICAL, true);
		
		pw.println("private " + returnType + " " + convertMethod + "(" + getNameTypes().toType(dtoType).toString(null, ClassSerializer.CANONICAL, true) + " instance) {");

		Set<ConverterParameter> converterParameters = getConverterParameters(converter, parameters);

		pw.print("return (" + returnType + ") new " + converter.getCanonicalName() + "(");

		int i = 0;
		for (ConverterParameter parameter : converterParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(getParameterName(parameter));
			i++;
		}

		pw.println(").fromDto(instance);");
		pw.println("}");
		pw.println();

		return convertMethod;
	}

	protected String printToDtoConvertMethod(TypeMirror dtoType, TypeElement remoteServiceElement, Set<ConverterParameter> parameters, PrintWriter pw) {
		NamedType converter = toHelper.getDtoMappingClass(dtoType, remoteServiceElement, DtoMappingType.CONVERTER);

		if (converter == null) {
			return null;
		}

		NamedType domainClass = toHelper.getDtoMappingClass(dtoType, remoteServiceElement, DtoMappingType.DOMAIN);

		String convertMethod = getToDtoConverterMethodName(domainClass);

		String returnType = getNameTypes().toType(dtoType).toString(null, ClassSerializer.CANONICAL, true);
		
		pw.println("private " + returnType + " " + convertMethod + "(" + getNameTypes().toType(domainClass).toString(null, ClassSerializer.CANONICAL, true) + " instance) {");

		Set<ConverterParameter> converterParameters = getConverterParameters(converter, parameters);

		pw.print("return (" + returnType + ")new " + converter.getCanonicalName() + "(");

		int i = 0;
		for (ConverterParameter parameter : converterParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(getParameterName(parameter));
			i++;
		}

		pw.println(").toDto(instance);");
		pw.println("}");
		pw.println();

		return convertMethod;
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
					NamedType domainClass = toHelper.getDtoMappingClass(dtoParameter.asType(), remoteServiceElement, DtoMappingType.DOMAIN);
					if (!getNameTypes().toType(method.getParameters().get(index).asType()).equals(domainClass)) {
						pairMethod = false;
						break;
					}
					index++;
				}
			}

			if (pairMethod) {
				NamedType domainClass = toHelper.getDtoMappingClass(method.getReturnType(), remoteServiceElement, DtoMappingType.DOMAIN);
				if (getNameTypes().toType(method.getReturnType()).equals(domainClass)) {
					return method;
				}
				processingEnv.getMessager().printMessage(
						Kind.ERROR,
						"[ERROR] Service method return type does not fit the remote intereface definition " + remoteMethod.toString()
								+ ". This should never happend, you are probably magician or there is a bug in the sesam processor itself.",
						serviceElement);
			}
		}

		return null;
	}
}