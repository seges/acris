package sk.seges.sesam.pap.service;

import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.annotation.LocalServiceConverter;
import sk.seges.sesam.pap.service.configurer.ServiceConverterProcessorConfigurer;
import sk.seges.sesam.pap.service.model.ConverterParametersFilter;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.ParametersFilter;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.ConverterParameterFieldPrinter;
import sk.seges.sesam.pap.service.printer.LocalServiceFieldPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorBodyPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorDefinitionPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConverterProviderPrinter;
import sk.seges.sesam.pap.service.printer.ServiceMethodConverterPrinter;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;
import sk.seges.sesam.pap.service.provider.ServiceCollectorConfigurationProvider;
import sk.seges.sesam.pap.service.resolver.ServiceParametersResolver;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceConverterProcessor extends MutableAnnotationProcessor {

	private static final String SERVICE_DELEGATE_NAME = "Service";

	protected ConverterProviderPrinter converterProviderPrinter;
	protected TransferObjectProcessingEnvironment processingEnv;

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ServiceConverterProcessorConfigurer();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
				new ServiceTypeElement(context.getTypeElement(), processingEnv).getServiceConverter()
		};
	}

	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	protected ConfigurationProvider[] getConfigurationProviders(ServiceTypeElement service) {
		return new ConfigurationProvider[] {
				new ServiceCollectorConfigurationProvider(getClassPathTypes(), service, processingEnv, roundEnv)
		};
	}

	@Override
	protected void printAnnotations(ProcessorContext context) {

		List<LocalServiceTypeElement> localServiceInterfaces = new ServiceTypeElement(context.getTypeElement(), processingEnv).getLocalServiceInterfaces();

		FormattedPrintWriter pw = context.getPrintWriter();
		
		pw.print("@", LocalServiceConverter.class, "(remoteServices = ");

		if (localServiceInterfaces.size() > 0) {
			pw.print("{");
		}
		
		int i = 0;
		for (LocalServiceTypeElement localService: localServiceInterfaces) {
			if (i > 0) {
				pw.println();
				pw.print("			, ");
			}
			pw.print(localService.getRemoteServiceInterface(), ".class");
			i++;
		}
		
		if (localServiceInterfaces.size() > 0) {
			pw.print("}");
		}
		pw.println(")");

		super.printAnnotations(context);
	}
	
	protected ParametersFilter getParametersFilter() {
		return new ConverterParametersFilter();
	}
	
	protected ServiceConverterElementPrinter[] getElementPrinters(FormattedPrintWriter pw, ServiceTypeElement serviceTypeElement) {
		return new ServiceConverterElementPrinter[] {
				new LocalServiceFieldPrinter(pw),
				new ConverterParameterFieldPrinter(processingEnv, getParametersFilter(), getParametersResolver(), pw),
				new ServiceConstructorDefinitionPrinter(processingEnv, getParametersFilter(), getParametersResolver(), pw),
				new ServiceConstructorBodyPrinter(processingEnv, getParametersFilter(), getParametersResolver(), pw),
				new ServiceMethodConverterPrinter(processingEnv, getParametersResolver(), pw, converterProviderPrinter),
				new ServiceConverterProviderPrinter(processingEnv, getParametersResolver(), pw, converterProviderPrinter)
		};
	}

	@Override
	protected void init(Element element, RoundEnvironment roundEnv) {
		super.init(element, roundEnv);
		this.processingEnv = new TransferObjectProcessingEnvironment(super.processingEnv, roundEnv);
		ServiceTypeElement serviceTypeElement = new ServiceTypeElement((TypeElement) element, processingEnv);
		this.processingEnv.setConfigurationProviders(getConfigurationProviders(serviceTypeElement));
	}
	
	protected ConverterProviderPrinter getConverterProviderPrinter(FormattedPrintWriter pw) {
		return new ConverterProviderPrinter(pw, processingEnv, getParametersResolver());
	}
	
	@Override
	protected void processElement(ProcessorContext context) {

		FormattedPrintWriter pw = context.getPrintWriter();
		TypeElement element = context.getTypeElement();		
		ServiceTypeElement serviceTypeElement = new ServiceTypeElement(element, processingEnv);
		
		this.converterProviderPrinter = getConverterProviderPrinter(pw);

		for (ServiceConverterElementPrinter elementPrinter: getElementPrinters(pw, serviceTypeElement)) {
			elementPrinter.initialize(serviceTypeElement, context.getOutputType());
			for (LocalServiceTypeElement localServiceInterface: serviceTypeElement.getLocalServiceInterfaces()) {
				ServiceConverterPrinterContext printerContext = new ServiceConverterPrinterContext();
				printerContext.setLocalServiceFieldName(MethodHelper.toField(localServiceInterface.getSimpleName() + SERVICE_DELEGATE_NAME));
				printerContext.setLocalServiceInterface(localServiceInterface);
				printerContext.setService(serviceTypeElement);
				elementPrinter.print(printerContext);
			}
			elementPrinter.finish(serviceTypeElement);
		}
	
		this.converterProviderPrinter.printConverterMethods(true, 1);
	}

	protected ParametersResolver getParametersResolver() {
		return new ServiceParametersResolver(processingEnv);
	}	

	protected String getConverterMethodName(MutableDeclaredType domainClass) {
		return "getConverter";
	}

	protected String getParameterName(ConverterParameter parameter) {
		if (parameter.getSameParameter() != null) {
			return parameter.getSameParameter().getName();
		}

		return parameter.getName();
	}

}