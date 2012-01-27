package sk.seges.sesam.pap.service.printer;

import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.service.printer.model.NestedServiceConverterPrinterContext;

public class ServiceDomainConverterProviderPrinter extends AbstractServiceObjectConverterProviderPrinter {

	public ServiceDomainConverterProviderPrinter(TransferObjectProcessingEnvironment processingEnv,
			FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, pw, converterProviderPrinter);
	}

	private static final String DOMAIN_PARAMETER_NAME = "domain";

	@Override
	public void print(NestedServiceConverterPrinterContext context) {

		if (!types.contains(context.getRawDomain().getCanonicalName())) {

			if (types.size() == 0) {
				pw.println("public <" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + ", " + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX +
						"> ", getTypedDtoConverter(), " getConverterForDomain(" + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + " " + DOMAIN_PARAMETER_NAME + ") {");
			}
			
			types.add(context.getRawDomain().getCanonicalName());
			pw.println("if (" + DOMAIN_PARAMETER_NAME + " instanceof ", context.getRawDomain(), ") {");
			String fieldName = MethodHelper.toField(context.getRawDomain().getSimpleName());
			pw.println(context.getRawDomain(), " " + fieldName + " = (", context.getRawDomain(), ")" + DOMAIN_PARAMETER_NAME + ";");
			pw.print("return (", getTypedDtoConverter(), ") ");
			converterProviderPrinter.printDomainConverterMethodName(context.getConverterType(), context.getRawDomain(), fieldName, context.getLocalMethod(), pw);
			pw.println(";");
			pw.println("}");
			pw.println();
		}
	}
}