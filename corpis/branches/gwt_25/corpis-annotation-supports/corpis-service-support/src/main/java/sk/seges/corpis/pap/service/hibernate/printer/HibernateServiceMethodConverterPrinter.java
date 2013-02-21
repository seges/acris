package sk.seges.corpis.pap.service.hibernate.printer;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.corpis.pap.service.hibernate.accessor.TransactionPropagationAccessor;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.service.printer.ServiceMethodConverterPrinter;

public class HibernateServiceMethodConverterPrinter extends ServiceMethodConverterPrinter {

	public HibernateServiceMethodConverterPrinter(TransferObjectProcessingEnvironment processingEnv, ConverterConstructorParametersResolverProvider parametersResolverProvider,
			ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, parametersResolverProvider, converterProviderPrinter);
	}

	protected Class<?>[] getSupportedAnnotations(Element method) {
		TransactionPropagationAccessor transactionPropagationAccessor = new TransactionPropagationAccessor(method, processingEnv);

		List<Class<?>> result = new ArrayList<Class<?>>();
		
		Class<?>[] ignoredAnnotations = super.getSupportedAnnotations(method);
		
		if (ignoredAnnotations != null) {
			for (Class<?> ignoredAnnotation: ignoredAnnotations) {
				result.add(ignoredAnnotation);
			}
		}

		if (transactionPropagationAccessor.isTransactionPropagated()) {
			result.add(Transactional.class);
		}

		return result.toArray(new Class<?>[] {});
	}
}