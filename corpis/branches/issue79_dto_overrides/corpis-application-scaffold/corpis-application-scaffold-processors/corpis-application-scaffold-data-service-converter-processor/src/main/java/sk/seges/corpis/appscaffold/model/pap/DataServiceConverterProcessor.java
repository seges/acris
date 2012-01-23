package sk.seges.corpis.appscaffold.model.pap;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.appscaffold.model.pap.printer.HibernateDataServiceMethodConverterPrinter;
import sk.seges.corpis.appscaffold.model.pap.provider.DataServiceCollectorConfigurationProvider;
import sk.seges.corpis.pap.service.hibernate.HibernateServiceConverterProcessor;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.ConverterParameterFieldPrinter;
import sk.seges.sesam.pap.service.printer.LocalServiceFieldPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorBodyPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorDefinitionPrinter;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DataServiceConverterProcessor extends HibernateServiceConverterProcessor {

	@Override
	protected ConfigurationProvider[] getConfigurationProviders(ServiceTypeElement service) {
		return new ConfigurationProvider[] {
				new DataServiceCollectorConfigurationProvider(getClassPathTypes(), service, processingEnv, roundEnv)
		};
	}
	
	protected ServiceConverterElementPrinter[] getElementPrinters(FormattedPrintWriter pw) {
		return new ServiceConverterElementPrinter[] {
				new LocalServiceFieldPrinter(pw),
				new ConverterParameterFieldPrinter(processingEnv, getParametersFilter(), getParametersResolver(), pw),
				new ServiceConstructorDefinitionPrinter(processingEnv, getParametersFilter(), getParametersResolver(), pw),
				new ServiceConstructorBodyPrinter(processingEnv, getParametersFilter(), getParametersResolver(), pw),
				new HibernateDataServiceMethodConverterPrinter(processingEnv, getParametersResolver(), pw, converterProviderPrinter)
		};
	}
}
