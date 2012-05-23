package sk.seges.corpis.pap.model.printer.converter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

public class HibernateServiceConverterPrinter extends AbstractHibernateConverterProviderPrinter {

	private static final String THIS = "this";

	public HibernateServiceConverterPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv,
			ConverterConstructorParametersResolver parametersResolver) {
		super(pw, processingEnv, parametersResolver);
	}

	@Override
	protected MutableReferenceType getConverterProviderReference() {
		return processingEnv.getTypeUtils().getReference(null, THIS);
	}
}