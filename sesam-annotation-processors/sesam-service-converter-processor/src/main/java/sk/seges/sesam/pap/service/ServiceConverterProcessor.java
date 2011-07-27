package sk.seges.sesam.pap.service;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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
import sk.seges.sesam.pap.service.utils.ServiceHelper;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceConverterProcessor extends AbstractConfigurableProcessor {

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

		//type should be always declared type. If not, than strange things happend
		TypeElement serviceElement = (TypeElement)((DeclaredType)mutableType.asType()).asElement();
		
		Element[] localInterfaces = serviceHelper.getLocalServiceInterfaces(serviceElement);
		
		if (localInterfaces == null || localInterfaces.length == 0) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find local interface for the service " + serviceElement + 
					". You should specify local service interface using " + LocalService.class.getCanonicalName() + " annotation.", serviceElement);
			return null;
		}

		List<NamedType> result = new ArrayList<NamedType>();
		
		for (Element localInterface: localInterfaces) {
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
	
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, PrintWriter pw) {

		Element localInterface = localInterfacesCache.get(outputClass);

		if (localInterface == null) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find local interface for the service " + element + 
					". Most probably you found a bug in the sesam, report this bug immediately. Or not :-).", element);
			return;
		}
		
		//TODO additional constructor parameters + service in the constructor
		methodHelper.copyConstructors(outputClass, element, pw);
		copyMethods(element, localInterface, pw);
	}
	
	protected void copyMethods(Element element, Element localInterface, PrintWriter pw) {
		
		TypeElement remoteServiceInterface = (TypeElement)serviceHelper.getRemoteServiceInterface(localInterface);

		if (remoteServiceInterface == null) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find remote service pair for the local service definition " +
					localInterface.toString(), element);
			return;
		}

		List<ExecutableElement> remoteMethods = ElementFilter.methodsIn(remoteServiceInterface.getEnclosedElements());

		Map<TypeMirror, String> dtoConverters = new HashMap<TypeMirror, String>();
		Map<TypeMirror, String> domainConverters = new HashMap<TypeMirror, String>();
		
		for (ExecutableElement remoteMethod: remoteMethods) {
			ExecutableElement localMethod = getDomainMethodPair(remoteMethod, element, remoteServiceInterface);
			if (localMethod == null) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Service class does not implements local service method " + remoteMethod.toString() + 
						". Please specify correct service implementation.", element);
				continue;
			}

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID) && !dtoConverters.containsKey(remoteMethod.getReturnType())) {
				String convertToDtoMethodName = printToDtoConvertMethod(remoteMethod.getReturnType(), remoteServiceInterface, pw);
				dtoConverters.put(remoteMethod.getReturnType(), convertToDtoMethodName);
			}

			for (VariableElement localMethodParameter: localMethod.getParameters()) {
				if (!domainConverters.containsKey(localMethodParameter.asType())) {
					String convertFromDtoMethodName = printFromDtoConvertMethod(localMethodParameter.asType(), remoteServiceInterface, pw);
					domainConverters.put(localMethodParameter.asType(), convertFromDtoMethodName);
				}
			}

			methodHelper.copyAnnotations(localMethod, pw);
			methodHelper.copyMethodDefinition(remoteMethod, ClassSerializer.CANONICAL, pw);
			pw.println("{");

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				if (dtoConverters.get(remoteMethod.getReturnType()) == null) {
					pw.print("return ");
				} else {
					pw.print("return "  + dtoConverters.get(remoteMethod.getReturnType()) + "(");
				}
			}
			
			pw.print(SERVICE_DELEGATE_NAME + "." + localMethod.getSimpleName().toString() + "(");
			
			int i = 0;
			for (VariableElement localMethodParameter: localMethod.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				
				String domainConverter = domainConverters.get(localMethodParameter.asType());
				
				if (domainConverter == null) {
					pw.print(domainConverter + "("); 
				}

				pw.print(localMethodParameter.getSimpleName().toString());
				
				if (domainConverter == null) {
					pw.print(domainConverter + ")"); 
				}
			}

			pw.print(")");
			
			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID) && dtoConverters.get(remoteMethod.getReturnType()) != null) {
				pw.print(")");
			}
			pw.println(";");
			
			pw.println("}");
		}
	}

	protected String printFromDtoConvertMethod(TypeMirror dtoType, TypeElement remoteServiceElement, PrintWriter pw) {
		NamedType converter = toHelper.getDtoMappingClass(dtoType, remoteServiceElement, DtoMappingType.CONVERTER);
		
		if (converter == null) {
			return null;
		}

		NamedType domainClass = toHelper.getDtoMappingClass(dtoType, remoteServiceElement, DtoMappingType.DOMAIN);
		
		String convertMethod = "convertFromDto";
		
		pw.println("private " + domainClass.getCanonicalName() + " " + convertMethod + "(" + dtoType.toString() + " instance) {");
		//TODO, this won't be so easy, converter probably will need any parameters
		pw.println("return new " + converter.getCanonicalName() + "().fromDto(instance);");
		pw.println("}");
		pw.println();
		
		return convertMethod;
	}

	protected String printToDtoConvertMethod(TypeMirror dtoType, TypeElement remoteServiceElement, PrintWriter pw) {
		NamedType converter = toHelper.getDtoMappingClass(dtoType, remoteServiceElement, DtoMappingType.CONVERTER);
		
		if (converter == null) {
			return null;
		}

		NamedType domainClass = toHelper.getDtoMappingClass(dtoType, remoteServiceElement, DtoMappingType.DOMAIN);
		
		String convertMethod = "convertToDto";
		
		pw.println("private " + dtoType.toString() + " " + convertMethod + "(" + domainClass.getCanonicalName() + " instance) {");
		//TODO, this won't be so easy, converter probably will need any parameters
		pw.println("return new " + converter.getCanonicalName() + "().toDto(instance);");
		pw.println("}");
		pw.println();
		
		return convertMethod;
	}

	protected ExecutableElement getDomainMethodPair(ExecutableElement remoteMethod, Element serviceElement, TypeElement remoteServiceElement) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(serviceElement.getEnclosedElements());
		
		for (ExecutableElement method: methods) {
			boolean pairMethod = true;

			if (method.getSimpleName().toString().equals(remoteMethod.getSimpleName().toString()) && 
				method.getParameters().size() == remoteMethod.getParameters().size()) {
				int index = 0;
				for (VariableElement dtoParameter: remoteMethod.getParameters()) {
					NamedType domainClass = toHelper.getDtoMappingClass(dtoParameter.asType(), remoteServiceElement, DtoMappingType.DOMAIN);
					if (getNameTypes().toType(method.getParameters().get(index)).equals(domainClass)) {
						pairMethod = false;
						break;
					}
					index++;
				}
			}

			if (pairMethod) {
				NamedType domainClass = toHelper.getDtoMappingClass(method.getReturnType(), remoteServiceElement, DtoMappingType.DOMAIN);
				if (getNameTypes().toType(remoteMethod.getReturnType()).equals(domainClass)) {
					return method;
				}
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Service method return type does not fit the remote intereface definition " + remoteMethod.toString() + 
						". This should never happend, you are probably magician or there is a bug in the sesam processor itself.", serviceElement);
			}
		}
		
		return null;
	}
	
}