package sk.seges.sesam.pap.service.printer.converterprovider;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.printer.converterprovider.AbstractDomainMethodConverterProviderPrinter;
import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.Field;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterProviderPrinterContext;

public class ServiceMethodDomainConverterProviderPrinter extends AbstractDomainMethodConverterProviderPrinter {

	public ServiceMethodDomainConverterProviderPrinter(ConverterConstructorParametersResolverProvider parametersResolverProvider,
			TransferObjectProcessingEnvironment processingEnv, FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(parametersResolverProvider, processingEnv, pw, converterProviderPrinter);
	}

	protected void printResulConverter(ConverterProviderPrinterContext context) {
		pw.print("return (", getTypedDtoConverter(), ") ");
		
		MutableDeclaredType fieldType = processingEnv.getTypeUtils().getDeclaredType(processingEnv.getTypeUtils().toMutableType(Class.class), 
				processingEnv.getTypeUtils().getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX));

		Field field = new Field(DOMAIN_CLASS_PARAMETER_NAME, fieldType);
		field.setCastType(processingEnv.getTypeUtils().getDeclaredType(processingEnv.getTypeUtils().toMutableType(Class.class), new MutableDeclaredType[] { context.getRawDomain() }));

		converterProviderPrinter.printDomainGetConverterMethodName(context.getRawDomain(), field, 
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