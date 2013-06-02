package sk.seges.sesam.pap.service;

import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.AbstractTransferProcessingProcessor;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterInstancerType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.model.resolver.CacheableConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.annotation.LocalServiceConverter;
import sk.seges.sesam.pap.service.configurer.ServiceConverterProcessorConfigurer;
import sk.seges.sesam.pap.service.model.DefaultServiceConverterParametersFilter;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceConverterParametersFilter;
import sk.seges.sesam.pap.service.model.ServiceConverterTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.ConverterParameterFieldPrinter;
import sk.seges.sesam.pap.service.printer.LocalServiceFieldPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorBodyPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorDefinitionPrinter;
import sk.seges.sesam.pap.service.printer.ServiceMethodConverterPrinter;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.converterprovider.ServiceConverterProviderContextPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;
import sk.seges.sesam.pap.service.provider.ServiceCollectorConfigurationProvider;
import sk.seges.sesam.pap.service.resolver.ServiceConverterConstructorParametersResolver;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceConverterProcessor extends AbstractTransferProcessingProcessor {

	private static final String SERVICE_DELEGATE_NAME = "Service";

	class ServiceConverterProcessorParameterResolverProvider extends CacheableConverterConstructorParametersResolverProvider {

		@Override
		public ConverterConstructorParametersResolver constructParameterResolver(UsageType usageType) {
			switch (usageType) {
				case CONVERTER_PROVIDER_OUTSIDE_USAGE:
					return new ServiceConverterConstructorParametersResolver(processingEnv) {
						
						@Override
						protected MutableReferenceType getConverterProviderContextReference() {
							return processingEnv.getTypeUtils().getReference(null, THIS);
						}
					};
				default:
					return new ServiceConverterConstructorParametersResolver(processingEnv);
			}
		}
		
	}
	
	protected ConverterProviderPrinter converterProviderPrinter;

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


	@Override
	protected ConfigurationProvider[] getConfigurationProviders(MutableDeclaredType service, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
		return new ConfigurationProvider[] {
				new ServiceCollectorConfigurationProvider((ServiceTypeElement)service, getClassPathTypes(), context)
		};
	}

	@Override
	protected void printAnnotations(ProcessorContext context) {

		List<LocalServiceTypeElement> localServiceInterfaces = new ServiceTypeElement(context.getTypeElement(), processingEnv).getLocalServiceInterfaces();

		FormattedPrintWriter pw = context.getPrintWriter();

		//TODO use annotation printer, create MutableAnnotationMirror
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
			pw.print(localService.getRemoteServiceInterface().clone().stripTypeParameters(), ".class");
			i++;
		}
		
		if (localServiceInterfaces.size() > 0) {
			pw.print("}");
		}
		pw.println(")");

		super.printAnnotations(context);
	}
	
	protected ServiceConverterParametersFilter getParametersFilter() {
		return new DefaultServiceConverterParametersFilter();
	}
	
	protected ServiceConverterElementPrinter[] getElementPrinters(ServiceTypeElement serviceTypeElement) {
		return new ServiceConverterElementPrinter[] {
				new LocalServiceFieldPrinter(),
				new ConverterParameterFieldPrinter(processingEnv, getParametersFilter(), getParametersResolverProvider(serviceTypeElement)),
				new ServiceConstructorDefinitionPrinter(processingEnv, getParametersFilter(), getParametersResolverProvider(serviceTypeElement)),
				new ServiceConstructorBodyPrinter(processingEnv, getParametersFilter(), getParametersResolverProvider(serviceTypeElement)),
				new ServiceMethodConverterPrinter(processingEnv, getParametersResolverProvider(serviceTypeElement), converterProviderPrinter),
				new ServiceConverterProviderContextPrinter(processingEnv, getParametersResolverProvider(serviceTypeElement), converterProviderPrinter, getClassPathTypes())
		};
	}

	protected ConverterProviderPrinter getConverterProviderPrinter(ServiceTypeElement serviceTypeElement) {
		return new ConverterProviderPrinter(processingEnv, getParametersResolverProvider(serviceTypeElement), UsageType.CONVERTER_PROVIDER_OUTSIDE_USAGE);
	}
	
	@Override
	protected void processElement(ProcessorContext context) {

		ServiceConverterTypeElement  converterType = (ServiceConverterTypeElement) context.getOutputType();
		ServiceTypeElement serviceTypeElement = converterType.getServiceTypeElement();
		
		this.converterProviderPrinter = getConverterProviderPrinter(serviceTypeElement);

		for (ServiceConverterElementPrinter elementPrinter: getElementPrinters(serviceTypeElement)) {
			elementPrinter.initialize(serviceTypeElement, context.getOutputType());
			for (LocalServiceTypeElement localServiceInterface: serviceTypeElement.getLocalServiceInterfaces()) {
				ServiceConverterPrinterContext printerContext = new ServiceConverterPrinterContext(processingEnv);
				printerContext.setLocalServiceFieldName(MethodHelper.toField(localServiceInterface.getSimpleName() + SERVICE_DELEGATE_NAME));
				printerContext.setLocalServiceInterface(localServiceInterface);
				printerContext.setService(serviceTypeElement);
				elementPrinter.print(printerContext);
			}
			elementPrinter.finish(serviceTypeElement);
		}

		this.converterProviderPrinter.printConverterMethods(context.getOutputType(), true, ConverterInstancerType.SERVICE_CONVERETR_INSTANCER);
	}

	@Override
	protected MutableDeclaredType getTargetType(TypeElement element) {
		return new ServiceTypeElement(element, processingEnv);
	}

	protected ConverterConstructorParametersResolverProvider getParametersResolverProvider(ServiceTypeElement serviceTypeElement) {
		return new ServiceConverterProcessorParameterResolverProvider();
	}
	
	protected String getConverterMethodName(MutableDeclaredType domainClass) {
		return "getConverter";
	}

	protected String getParameterName(ConverterConstructorParameter parameter) {
		if (parameter.getSameParameter() != null) {
			return parameter.getSameParameter().getName();
		}

		return parameter.getName();
	}
}