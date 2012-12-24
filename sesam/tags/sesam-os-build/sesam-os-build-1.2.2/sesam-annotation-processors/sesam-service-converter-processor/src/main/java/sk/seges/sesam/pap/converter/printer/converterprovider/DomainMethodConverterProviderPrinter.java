package sk.seges.sesam.pap.converter.printer.converterprovider;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.Field;
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
		
		MutableDeclaredType fieldType = processingEnv.getTypeUtils().getDeclaredType(processingEnv.getTypeUtils().toMutableType(Class.class), 
				processingEnv.getTypeUtils().getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX));
		Field field = new Field(DOMAIN_CLASS_PARAMETER_NAME, fieldType);
		field.setCastType(processingEnv.getTypeUtils().getDeclaredType(processingEnv.getTypeUtils().toMutableType(Class.class), new MutableDeclaredType[] { context.getDomain() }));
		converterProviderPrinter.printDomainGetConverterMethodName(context.getRawDomain(), field, null, pw, false);
	}
}