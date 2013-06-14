package sk.seges.sesam.pap.service;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
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
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceConverterTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;
import sk.seges.sesam.pap.service.provider.ServiceCollectorConfigurationProvider;
import sk.seges.sesam.pap.service.resolver.ServiceConverterConstructorParametersResolver;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public abstract class AbstractServiceConverterBaseProcessor extends AbstractTransferProcessingProcessor {

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
	protected ConfigurationProvider[] getConfigurationProviders(MutableDeclaredType service, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
		return new ConfigurationProvider[] {
				new ServiceCollectorConfigurationProvider((ServiceTypeElement)service, getClassPathTypes(), context)
		};
	}

	protected abstract ServiceConverterElementPrinter[] getElementPrinters(ServiceTypeElement serviceTypeElement);

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

	protected ConverterConstructorParametersResolverProvider getParametersResolverProvider(ServiceTypeElement serviceTypeElement) {
		return new ServiceConverterProcessorParameterResolverProvider();
	}
}