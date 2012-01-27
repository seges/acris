package sk.seges.corpis.pap.model.printer.converter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class HibernateServiceConverterPrinter extends AbstractHibernateConverterProviderPrinter {

	private static final String THIS = "this";

	public HibernateServiceConverterPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv,
			ParametersResolver parametersResolver) {
		super(pw, processingEnv, parametersResolver);
	}

	@Override
	protected MutableReferenceType getConverterProviderReference() {
		return processingEnv.getTypeUtils().getReference(null, THIS);
	}
}