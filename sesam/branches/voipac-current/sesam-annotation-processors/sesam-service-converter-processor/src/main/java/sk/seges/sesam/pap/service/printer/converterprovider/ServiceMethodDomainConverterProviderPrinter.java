package sk.seges.sesam.pap.service.printer.converterprovider;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.printer.converterprovider.AbstractDomainMethodConverterProviderPrinter;
import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterProviderPrinterContext;

public class ServiceMethodDomainConverterProviderPrinter extends AbstractDomainMethodConverterProviderPrinter {

	public ServiceMethodDomainConverterProviderPrinter(ConverterConstructorParametersResolver parametersResolver,
			TransferObjectProcessingEnvironment processingEnv, FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(parametersResolver, processingEnv, pw, converterProviderPrinter);
	}

	protected void printResulConverter(ConverterProviderPrinterContext context) {
		pw.print("return (", getTypedDtoConverter(), ") ");
		converterProviderPrinter.printDomainGetConverterMethodName(context.getRawDomain(), 
				"(" + Class.class.getSimpleName() + "<" + context.getDomain().toString(ClassSerializer.SIMPLE, true) + ">)" + DOMAIN_CLASS_PARAMETER_NAME, 
				((ServiceConverterProviderPrinterContext)context).getLocalMethod(), pw, false);
	}
	
	@Override
	protected void printType(MutableTypeMirror type, ConverterProviderPrinterContext context) {

		DomainType domainType = processingEnv.getTransferObjectUtils().getDomainType(type);
		if (domainType.getKind().isDeclared() && domainType.getConverter() != null) {
			context = new ServiceConverterProviderPrinterContext((DomainDeclaredType)domainType, ((ServiceConverterProviderPrinterContext)context).getLocalMethod());
			print(context);
		}
	}
}