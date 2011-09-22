package sk.seges.corpis.pap.service.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.corpis.pap.service.annotation.TransactionPropagation;
import sk.seges.corpis.pap.service.annotation.TransactionPropagation.PropagationType;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.ServiceConverterProcessor;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateServiceConverterProcessor extends ServiceConverterProcessor {

	protected ParametersResolver getParametersResolver() {
		return new HibernateParameterResolver(processingEnv);
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
