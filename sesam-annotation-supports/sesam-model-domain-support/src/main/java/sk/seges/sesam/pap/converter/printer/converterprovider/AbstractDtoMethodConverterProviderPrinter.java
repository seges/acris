package sk.seges.sesam.pap.converter.printer.converterprovider;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.printer.AbstractObjectConverterProviderPrinter;
import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;

public abstract class AbstractDtoMethodConverterProviderPrinter extends AbstractObjectConverterProviderPrinter {

	protected static final String DTO_CLASS_PARAMETER_NAME = "dto";

	public AbstractDtoMethodConverterProviderPrinter(TransferObjectProcessingEnvironment processingEnv, FormattedPrintWriter pw,
			ConverterProviderPrinter converterProviderPrinter, ConverterConstructorParametersResolver parametersResover) {
		super(processingEnv, pw, converterProviderPrinter, parametersResover);
	}

	@Override
	public void print(ConverterProviderPrinterContext context) {
		
		if (context.getConverterType() == null) {
			return;
		}
		
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
			pw.println("if (" + rawDtoName + ".class.isAssignableFrom(" + DTO_CLASS_PARAMETER_NAME + ")) {");

			printResultConverter(context);
			
			pw.println(";");
			pw.println("}");
			pw.println();
		}

		printTypeVariables(context);
	}

	protected abstract void printResultConverter(ConverterProviderPrinterContext context);
}
