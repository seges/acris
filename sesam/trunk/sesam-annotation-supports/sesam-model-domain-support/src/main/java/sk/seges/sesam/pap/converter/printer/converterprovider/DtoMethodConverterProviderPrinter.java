package sk.seges.sesam.pap.converter.printer.converterprovider;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;

public class DtoMethodConverterProviderPrinter extends AbstractDtoMethodConverterProviderPrinter {

	public DtoMethodConverterProviderPrinter(ConverterConstructorParametersResolverProvider parametersResoverProvider, 
			TransferObjectProcessingEnvironment processingEnv, FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, pw, converterProviderPrinter, parametersResoverProvider);
	}

	@Override
	protected void printResultConverter(ConverterProviderPrinterContext context) {
		pw.print("return (", getTypedDtoConverter(), ") ");
		converterProviderPrinter.printDtoGetConverterMethodName(context.getRawDto(), null, null, pw, false);
	}

	@Override
	protected void printType(MutableTypeMirror type, ConverterProviderPrinterContext context) {

		DtoType dtoType = processingEnv.getTransferObjectUtils().getDtoType(type);
		if (dtoType.getKind().isDeclared() && dtoType.getConverter() != null) {
			print(new ConverterProviderPrinterContext((DtoDeclaredType)dtoType));
		}
	}
}