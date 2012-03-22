package sk.seges.sesam.pap.service.printer;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.printer.model.NestedServiceConverterPrinterContext;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;

public class ServiceDomainConverterProviderPrinter extends AbstractServiceObjectConverterProviderPrinter {

	public ServiceDomainConverterProviderPrinter(ConverterConstructorParametersResolver parametersResolver, TransferObjectProcessingEnvironment processingEnv,
			FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, pw, converterProviderPrinter, parametersResolver);
	}

	private static final String DOMAIN_CLASS_PARAMETER_NAME = "domainClass";

	@Override
	public void print(NestedServiceConverterPrinterContext context) {

		if (!types.contains(context.getRawDomain().getCanonicalName())) {

			if (types.size() == 0) {
				String cacheParameterName = getConstructorParameterName(processingEnv.getTypeUtils().toMutableType(ConvertedInstanceCache.class));
				
				pw.println("public <" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + ", " + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX +
						"> ", getTypedDtoConverter(), " getConverterForDomain(", Class.class.getSimpleName(), "<" + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "> " + DOMAIN_CLASS_PARAMETER_NAME + 
						", ", ConvertedInstanceCache.class, " " + cacheParameterName + ") {");
				pw.println();
				pw.println("if (" + DOMAIN_CLASS_PARAMETER_NAME + " == null) {");
				pw.println("return null;");
				pw.println("}");
				pw.println();
			}
			
			types.add(context.getRawDomain().getCanonicalName());
			
			String rawDomainClass = context.getRawDomain().toString(ClassSerializer.SIMPLE, false);
//			pw.println("if (" + DOMAIN_PARAMETER_NAME + " instanceof ", context.getRawDomain().toString(ClassSerializer.SIMPLE, false), ") {");
			pw.println("if (" + rawDomainClass + ".class.isAssignableFrom(" + DOMAIN_CLASS_PARAMETER_NAME + ")) {");
			//String fieldName = MethodHelper.toField(context.getRawDomain().getSimpleName());
			//pw.println(context.getRawDomain(), " " + fieldName + " = (", context.getRawDomain(), ")" + DOMAIN_PARAMETER_NAME + ";");
			pw.print("return (", getTypedDtoConverter(), ") ");
			converterProviderPrinter.printDomainGetConverterMethodName(context.getRawDomain(), 
					"(" + Class.class.getSimpleName() + "<" + context.getDomain().toString(ClassSerializer.SIMPLE, true) + ">)" + DOMAIN_CLASS_PARAMETER_NAME, context.getLocalMethod(), pw, false);
			pw.println(";");
			pw.println("}");
			pw.println();
		}
	}
}