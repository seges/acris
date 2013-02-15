package sk.seges.sesam.pap.converter.printer.converterprovider;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

public class DomainMethodConverterProviderPrinter extends AbstractDomainMethodConverterProviderPrinter {

	public DomainMethodConverterProviderPrinter(ConverterConstructorParametersResolver parametersResolver, TransferObjectProcessingEnvironment processingEnv,
			FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(parametersResolver, processingEnv, pw, converterProviderPrinter);
	}

	@Override
	protected void printResulConverter(ConverterProviderPrinterContext context) {
		pw.print("return (", getTypedDtoConverter(), ") ");
		converterProviderPrinter.printDomainGetConverterMethodName(context.getRawDomain(), 
				"(" + Class.class.getSimpleName() + "<" + context.getDomain().toString(ClassSerializer.SIMPLE, true) + ">)" + DOMAIN_CLASS_PARAMETER_NAME, null, pw, false);
	}
}