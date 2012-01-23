package sk.seges.corpis.pap.model.hibernate.printer.method;

import javax.annotation.processing.RoundEnvironment;

import sk.seges.corpis.pap.model.hibernate.resolver.HibernateEntityResolver;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyToDtoPrinter;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class HibernateCopyToDtoPrinter extends CopyToDtoPrinter {

	private final HibernateEntityResolver entityResolver;
	
	public HibernateCopyToDtoPrinter(ConverterProviderPrinter converterProviderPrinter, ElementHolderTypeConverter elementHolderTypeConverter,
			HibernateEntityResolver entityResolver, ParametersResolver parametersResolver, RoundEnvironment roundEnv,
			TransferObjectProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(converterProviderPrinter, elementHolderTypeConverter, entityResolver, parametersResolver, roundEnv, processingEnv, pw);
		this.entityResolver = entityResolver;
	}

	@Override
	public void print(TransferObjectContext context) {
		copy(context, pw, new HibernateCopyToDtoMethodPrinter(converterProviderPrinter, elementHolderTypeConverter, entityResolver, parametersResolver, roundEnv, processingEnv));
	}
	
}
