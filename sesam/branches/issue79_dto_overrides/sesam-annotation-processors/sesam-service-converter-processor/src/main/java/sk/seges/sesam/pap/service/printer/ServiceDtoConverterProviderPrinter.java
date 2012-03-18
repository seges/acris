package sk.seges.sesam.pap.service.printer;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.service.printer.model.NestedServiceConverterPrinterContext;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;

public class ServiceDtoConverterProviderPrinter extends AbstractServiceObjectConverterProviderPrinter {

	public ServiceDtoConverterProviderPrinter(TransferObjectProcessingEnvironment processingEnv,
			FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, pw, converterProviderPrinter);
	}

	private static final String DTO_CLASS_PARAMETER_NAME = "dto";

	@Override
	public void print(NestedServiceConverterPrinterContext context) {
		
		if (!types.contains(context.getRawDto().getCanonicalName())) {
			
			if (types.size() == 0) {
				String cacheParameterName = getConstructorParameterName(processingEnv.getTypeUtils().toMutableType(ConvertedInstanceCache.class));
				
				pw.println("public <"	+ ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + ", " + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX +
						"> ", getTypedDtoConverter(), " getConverterForDto(", Class.class.getSimpleName(), "<" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + "> " + DTO_CLASS_PARAMETER_NAME +
						", ", ConvertedInstanceCache.class, " " + cacheParameterName + ") {");
				pw.println();
				pw.println("if (" + DTO_CLASS_PARAMETER_NAME + " == null) {");
				pw.println("return null;");
				pw.println("}");
				pw.println();
			}
			
			String rawDtoName = context.getRawDto().toString(ClassSerializer.SIMPLE, false);
			
			types.add(context.getRawDto().getCanonicalName());
//			pw.println("if (" + DTO_PARAMETER_NAME + " instanceof ", context.getRawDto().toString(ClassSerializer.SIMPLE, false), ") {");
			pw.println("if (" + rawDtoName + ".class.isAssignableFrom(" + DTO_CLASS_PARAMETER_NAME + ")) {");
//			String fieldName = MethodHelper.toField(context.getRawDto().getSimpleName());
			//pw.println(context.getRawDto(), " " + fieldName + " = (", context.getRawDto(), ")" + DTO_PARAMETER_NAME + ";");
			pw.print("return (", getTypedDtoConverter(), ") ");
			converterProviderPrinter.printDtoGetConverterMethodName(context.getRawDto(), 
					"(" + Class.class.getSimpleName() + "<" + context.getDto().toString(ClassSerializer.SIMPLE, true) + ">)" + DTO_CLASS_PARAMETER_NAME, context.getLocalMethod(), pw);
			pw.println(";");
			pw.println("}");
			pw.println();
		}
	}
}