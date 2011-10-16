package sk.seges.corpis.pap.service.hibernate.printer;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.corpis.service.annotation.TransactionPropagation;
import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.printer.ServiceMethodConverterPrinter;

public class HibernateServiceMethodConverterPrinter extends ServiceMethodConverterPrinter {

	public HibernateServiceMethodConverterPrinter(TransferObjectProcessingEnvironment processingEnv, ParametersResolver parametersResolver,
			FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, parametersResolver, pw, converterProviderPrinter);
	}

	protected Class<?>[] getIgnoredAnnotations(Element method) {
		TransactionPropagation transactionPropagation = method.getAnnotation(TransactionPropagation.class);

		List<Class<?>> result = new ArrayList<Class<?>>();
		
		Class<?>[] ignoredAnnotations = super.getIgnoredAnnotations(method);
		
		if (ignoredAnnotations != null) {
			for (Class<?> ignoredAnnotation: ignoredAnnotations) {
				result.add(ignoredAnnotation);
			}
		}
		
		if (transactionPropagation != null && transactionPropagation.value().equals(PropagationType.ISOLATE)) {
			result.add(Transactional.class);
		}

		return result.toArray(new Class<?>[] {});
	}
}
