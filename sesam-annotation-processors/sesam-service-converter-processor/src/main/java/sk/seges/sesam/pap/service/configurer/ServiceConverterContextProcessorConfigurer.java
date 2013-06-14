package sk.seges.sesam.pap.service.configurer;

import sk.seges.sesam.core.pap.configuration.DelegateProcessorConfigurer;
import sk.seges.sesam.pap.service.annotation.LocalServiceConverterContext;
import sk.seges.sesam.pap.service.annotation.LocalServiceConverterContextDelegate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class ServiceConverterContextProcessorConfigurer extends DelegateProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { LocalServiceConverterContext.class };
		}
		return new Type[] {};
	}

	@Override
	protected Class<? extends Annotation> getDelegatedAnnotationClass() {
		return LocalServiceConverterContextDelegate.class;
	}

	@Override
	protected Annotation getAnnotationFromDelegate(Annotation annotationDelegate) {
		if (annotationDelegate instanceof LocalServiceConverterContextDelegate) {
			return ((LocalServiceConverterContextDelegate)annotationDelegate).value();
		}
		return null;
	}

}