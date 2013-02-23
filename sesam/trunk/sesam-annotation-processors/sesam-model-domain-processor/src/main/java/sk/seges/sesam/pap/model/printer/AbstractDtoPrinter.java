package sk.seges.sesam.pap.model.printer;

import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.AbstractConverterPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;

public class AbstractDtoPrinter extends AbstractConverterPrinter {

	protected AbstractDtoPrinter(ConverterConstructorParametersResolverProvider parametersResolverProvider, TransferObjectProcessingEnvironment processingEnv) {
		super(parametersResolverProvider, processingEnv);
	}	
}