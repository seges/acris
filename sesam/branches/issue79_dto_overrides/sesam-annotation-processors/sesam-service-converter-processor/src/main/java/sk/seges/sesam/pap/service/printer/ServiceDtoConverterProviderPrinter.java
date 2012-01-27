package sk.seges.sesam.pap.service.printer;

import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.service.printer.model.NestedServiceConverterPrinterContext;

public class ServiceDtoConverterProviderPrinter extends AbstractServiceObjectConverterProviderPrinter {

	public ServiceDtoConverterProviderPrinter(TransferObjectProcessingEnvironment processingEnv,
			FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, pw, converterProviderPrinter);
	}

	private static final String DTO_PARAMETER_NAME = "dto";

	@Override
	public void print(NestedServiceConverterPrinterContext context) {
		
		if (!types.contains(context.getRawDto().getCanonicalName())) {
			
			if (types.size() == 0) {
				pw.println("public <" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + ", " + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX +
						"> ", getTypedDtoConverter(), " getConverterForDto(" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + " " + DTO_PARAMETER_NAME + ") {");
			}
			
			types.add(context.getRawDto().getCanonicalName());
			pw.println("if (" + DTO_PARAMETER_NAME + " instanceof ", context.getRawDto(), ") {");
			String fieldName = MethodHelper.toField(context.getRawDto().getSimpleName());
			pw.println(context.getRawDto(), " " + fieldName + " = (", context.getRawDto(), ")" + DTO_PARAMETER_NAME + ";");
			pw.print("return (", getTypedDtoConverter(), ") ");
			converterProviderPrinter.printDtoConverterMethodName(context.getConverterType(), context.getRawDto(), fieldName, context.getLocalMethod(), pw);
			pw.println(";");
			pw.println("}");
			pw.println();
		}
	}
}