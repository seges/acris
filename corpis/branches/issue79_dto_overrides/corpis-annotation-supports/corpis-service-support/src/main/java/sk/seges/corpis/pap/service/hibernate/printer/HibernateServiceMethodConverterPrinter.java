package sk.seges.corpis.pap.service.hibernate.printer;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.corpis.pap.service.hibernate.accessor.TransactionPropagationAccessor;
import sk.seges.corpis.service.annotation.TransactionPropagation;
import sk.seges.corpis.service.annotation.TransactionPropagations;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.printer.ServiceMethodConverterPrinter;

public class HibernateServiceMethodConverterPrinter extends ServiceMethodConverterPrinter {

	public HibernateServiceMethodConverterPrinter(TransferObjectProcessingEnvironment processingEnv, ConverterConstructorParametersResolver parametersResolver,
			FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, parametersResolver, pw, converterProviderPrinter);
	}

	protected Class<?>[] getIgnoredAnnotations(Element method) {
		TransactionPropagationAccessor transactionPropagationAccessor = new TransactionPropagationAccessor(method, processingEnv);

		List<Class<?>> result = new ArrayList<Class<?>>();
		
		Class<?>[] ignoredAnnotations = super.getIgnoredAnnotations(method);
		
		if (ignoredAnnotations != null) {
			for (Class<?> ignoredAnnotation: ignoredAnnotations) {
				result.add(ignoredAnnotation);
			}
		}

		result.add(TransactionPropagation.class);
		result.add(TransactionPropagations.class);
		
		if (!transactionPropagationAccessor.isTransactionPropagated()) {
			result.add(Transactional.class);
		}

		return result.toArray(new Class<?>[] {});
	}
}