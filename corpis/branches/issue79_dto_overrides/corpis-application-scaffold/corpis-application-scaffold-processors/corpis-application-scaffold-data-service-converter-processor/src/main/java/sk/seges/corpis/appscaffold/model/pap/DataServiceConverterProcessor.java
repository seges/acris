package sk.seges.corpis.appscaffold.model.pap;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.appscaffold.model.pap.provider.DataServiceCollectorConfigurationProvider;
import sk.seges.corpis.pap.service.hibernate.HibernateServiceConverterProcessor;
import sk.seges.corpis.pap.service.hibernate.printer.HibernateServiceMethodConverterPrinter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.EnvironmentContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.ConverterParameterFieldPrinter;
import sk.seges.sesam.pap.service.printer.LocalServiceFieldPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorBodyPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorDefinitionPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConverterProviderPrinter;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DataServiceConverterProcessor extends HibernateServiceConverterProcessor {

	@Override
	protected ConfigurationProvider[] getConfigurationProviders(ServiceTypeElement service, EnvironmentContext<TransferObjectProcessingEnvironment> context) {
		return new ConfigurationProvider[] {
				new DataServiceCollectorConfigurationProvider(service, getClassPathTypes(), context)
		};
	}

	@Override
	protected ServiceConverterElementPrinter[] getElementPrinters(FormattedPrintWriter pw, ServiceTypeElement serviceTypeElement) {
		return new ServiceConverterElementPrinter[] {
				new LocalServiceFieldPrinter(pw),
				new ConverterParameterFieldPrinter(processingEnv, getParametersFilter(), getParametersResolver(), pw),
				new ServiceConstructorDefinitionPrinter(processingEnv, getParametersFilter(), getParametersResolver(), pw),
				new ServiceConstructorBodyPrinter(processingEnv, getParametersFilter(), getParametersResolver(), pw),
				new HibernateServiceMethodConverterPrinter(processingEnv, getParametersResolver(), pw, getConverterProviderPrinter(pw, serviceTypeElement)),
				new ServiceConverterProviderPrinter(processingEnv, getParametersResolver(), pw, converterProviderPrinter)
		};
	}
}