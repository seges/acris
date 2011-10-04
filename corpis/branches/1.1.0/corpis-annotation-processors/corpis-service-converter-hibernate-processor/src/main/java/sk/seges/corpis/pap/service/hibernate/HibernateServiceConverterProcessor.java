package sk.seges.corpis.pap.service.hibernate;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.corpis.pap.service.hibernate.printer.HibernateServiceMethodConverterPrinter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.ServiceConverterProcessor;
import sk.seges.sesam.pap.service.printer.ConverterParameterFieldPrinter;
import sk.seges.sesam.pap.service.printer.LocalServiceFieldPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorBodyPrinter;
import sk.seges.sesam.pap.service.printer.ServiceConstructorDefinitionPrinter;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateServiceConverterProcessor extends ServiceConverterProcessor {

	protected ParametersResolver getParametersResolver() {
		return new HibernateParameterResolver(processingEnv);
	}
	
	protected ServiceConverterElementPrinter[] getElementPrinters(FormattedPrintWriter pw) {
		return new ServiceConverterElementPrinter[] {
				new LocalServiceFieldPrinter(pw),
				new ConverterParameterFieldPrinter(processingEnv, getParametersResolver(), pw),
				new ServiceConstructorDefinitionPrinter(processingEnv, getParametersResolver(), pw),
				new ServiceConstructorBodyPrinter(processingEnv, getParametersResolver(), pw),
				new HibernateServiceMethodConverterPrinter(processingEnv, getParametersResolver(), pw, converterProviderPrinter)
		};
	}
}
