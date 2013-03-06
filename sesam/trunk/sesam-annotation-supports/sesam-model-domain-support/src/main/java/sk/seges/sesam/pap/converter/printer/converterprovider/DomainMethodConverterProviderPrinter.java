package sk.seges.sesam.pap.converter.printer.converterprovider;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;

public class DomainMethodConverterProviderPrinter extends AbstractDomainMethodConverterProviderPrinter {

	public DomainMethodConverterProviderPrinter(ConverterConstructorParametersResolverProvider parametersResolverProvider, 
			TransferObjectProcessingEnvironment processingEnv, FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(parametersResolverProvider, processingEnv, pw, converterProviderPrinter);
	}

	@Override
	protected void printResulConverter(ConverterProviderPrinterContext context) {
		pw.print("return (", getTypedDtoConverter(), ") ");
		converterProviderPrinter.printDomainGetConverterMethodName(context.getRawDomain(), null, null, pw, false);
	}
}