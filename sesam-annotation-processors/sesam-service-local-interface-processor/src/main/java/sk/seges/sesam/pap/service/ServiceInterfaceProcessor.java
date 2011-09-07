package sk.seges.sesam.pap.service;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.DtoTypeElement;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;
import sk.seges.sesam.pap.service.provider.RemoteServiceCollectorConfigurationProvider;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceInterfaceProcessor extends AbstractConfigurableProcessor {

	private ConfigurationProvider[] configurationProviders;

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
		return new NamedType[] { new RemoteServiceTypeElement((TypeElement)((DeclaredType)mutableType.asType()).asElement(), processingEnv).getLocalServiceElement()};
	}

	@Override
	protected void writeClassAnnotations(Element el, NamedType outputName, PrintWriter pw) {
		pw.println("@" + LocalServiceDefinition.class.getSimpleName() + "(remoteService = " + el.toString() + ".class)");
	}
	
	@Override
	protected Type[] getImports(TypeElement remoteServiceInterfaceElement) {
		return new Type[] {
				LocalServiceDefinition.class				
		};
	}

	
	protected ConfigurationProvider[] getConfigurationProviders(RemoteServiceTypeElement remoteServiceInterfaceElement) {
		return new ConfigurationProvider[] {
				new RemoteServiceCollectorConfigurationProvider(remoteServiceInterfaceElement, processingEnv, roundEnv)
		};
	}

	@Override
	protected void processElement(TypeElement remoteServiceInterfaceElement, NamedType outputName, RoundEnvironment roundEnv, FormattedPrintWriter pw) {

		RemoteServiceTypeElement remoteServiceTypeElement = new RemoteServiceTypeElement(remoteServiceInterfaceElement, processingEnv);
		this.configurationProviders = getConfigurationProviders(remoteServiceTypeElement);

		List<ExecutableElement> methods = ElementFilter.methodsIn(remoteServiceInterfaceElement.getEnclosedElements());
		
		for (ExecutableElement method: methods) {
			DtoTypeElement dtoReturnType = new DtoTypeElement(method.getReturnType(), processingEnv, roundEnv, configurationProviders);
			
			pw.print(dtoReturnType.getDomainTypeElement(), " " + method.getSimpleName().toString() + "(");
			
			int i = 0;
			for (VariableElement parameter: method.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				
				DtoTypeElement dtoParamType = new DtoTypeElement(parameter.asType(), processingEnv, roundEnv, configurationProviders);

				pw.print(dtoParamType.getDomainTypeElement(), " " + parameter.getSimpleName().toString());
				i++;
			}
			
			pw.println(");");
			pw.println();
		}
	}
}