package sk.seges.sesam.pap.service;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.pap.model.model.DtoTypeElement;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceInterfaceProcessor extends AbstractConfigurableProcessor {
		
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ServiceInterfaceProcessorConfigurer();
	}
	
	@Override
	protected ElementKind getElementKind() {
		return ElementKind.INTERFACE;
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] { new RemoteServiceTypeElement(mutableType.asType(), processingEnv).getLocalServiceElement()};
	}

	@Override
	protected void writeClassAnnotations(Element el, NamedType outputName, PrintWriter pw) {
		pw.println("@" + LocalServiceDefinition.class.getSimpleName() + "(remoteService = " + el.toString() + ".class)");
	}
	
	@Override
	protected Type[] getImports(TypeElement remoteServiceInterfaceElement) {

		List<ExecutableElement> methodsIn = ElementFilter.methodsIn(remoteServiceInterfaceElement.getEnclosedElements());
		
		List<Type> imports = new ArrayList<Type>();
		
		for (ExecutableElement method: methodsIn) {
			
			imports.add(new DtoTypeElement(remoteServiceInterfaceElement, method.getReturnType(), processingEnv, roundEnv).getDomainTypeElement());
			for (VariableElement parameter: method.getParameters()) {
				imports.add(new DtoTypeElement(remoteServiceInterfaceElement, parameter.asType(), processingEnv, roundEnv).getDomainTypeElement());
			}
		}

		imports.add(LocalServiceDefinition.class);
		
		return imports.toArray(new Type[] {});
	}

	@Override
	protected void processElement(TypeElement remoteServiceInterfaceElement, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {

		List<ExecutableElement> methods = ElementFilter.methodsIn(remoteServiceInterfaceElement.getEnclosedElements());
		
		for (ExecutableElement method: methods) {
			DtoTypeElement dtoReturnType = new DtoTypeElement(remoteServiceInterfaceElement, method.getReturnType(), processingEnv, roundEnv);
			
			pw.print(dtoReturnType.getDomainTypeElement().toString(ClassSerializer.SIMPLE, true) + " " + method.getSimpleName().toString() + "(");
			
			int i = 0;
			for (VariableElement parameter: method.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				
				DtoTypeElement dtoParamType = new DtoTypeElement(remoteServiceInterfaceElement, parameter.asType(), processingEnv, roundEnv);

				pw.print(dtoParamType.getDomainTypeElement().toString(ClassSerializer.SIMPLE, true) + " " + parameter.getSimpleName().toString());
				i++;
			}
			
			pw.println(");");
			pw.println();
		}
	}
}