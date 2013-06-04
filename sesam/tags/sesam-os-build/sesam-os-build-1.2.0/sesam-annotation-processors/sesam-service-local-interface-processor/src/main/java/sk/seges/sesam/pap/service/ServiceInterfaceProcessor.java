package sk.seges.sesam.pap.service;

import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;
import sk.seges.sesam.pap.service.configurer.ServiceInterfaceProcessorConfigurer;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;
import sk.seges.sesam.pap.service.provider.RemoteServiceCollectorConfigurationProvider;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceInterfaceProcessor extends MutableAnnotationProcessor {

	protected TransferObjectProcessingEnvironment processingEnv;

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ServiceInterfaceProcessorConfigurer();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
				new RemoteServiceTypeElement(context.getTypeElement(), processingEnv).getLocalServiceElement()				
		};
	}
	
	@Override
	protected void printAnnotations(ProcessorContext context) {
		context.getPrintWriter().println("@", LocalServiceDefinition.class, "(remoteService = " + context.getTypeElement().toString() + ".class)");
	}
	
	protected ConfigurationProvider[] getConfigurationProviders(RemoteServiceTypeElement remoteServiceInterfaceElement) {
		return new ConfigurationProvider[] {
				new RemoteServiceCollectorConfigurationProvider(remoteServiceInterfaceElement, processingEnv, roundEnv)
		};
	}

	@Override
	protected void init(Element element, RoundEnvironment roundEnv) {
		super.init(element, roundEnv);
		RemoteServiceTypeElement remoteServiceTypeElement = new RemoteServiceTypeElement((TypeElement)element, super.processingEnv);
		this.processingEnv = new TransferObjectProcessingEnvironment(super.processingEnv, roundEnv);
		this.processingEnv.setConfigurationProviders(getConfigurationProviders(remoteServiceTypeElement));
	}

	@Override
	protected void processElement(ProcessorContext context) {

		List<ExecutableElement> methods = ElementFilter.methodsIn(context.getTypeElement().getEnclosedElements());
		
		FormattedPrintWriter pw = context.getPrintWriter();
		
		for (ExecutableElement method: methods) {
			DtoType dtoReturnType = processingEnv.getTransferObjectUtils().getDtoType(method.getReturnType());
			
			pw.print(dtoReturnType.getDomain(), " " + method.getSimpleName().toString() + "(");
			
			int i = 0;
			for (VariableElement parameter: method.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				
				DtoType dtoParamType = processingEnv.getTransferObjectUtils().getDtoType(parameter.asType());

				pw.print(dtoParamType.getDomain(), " " + parameter.getSimpleName().toString());
				i++;
			}
			
			pw.println(");");
			pw.println();
		}
	}
}