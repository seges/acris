package sk.seges.sesam.pap.service.configurer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.configuration.DelegateProcessorConfigurer;
import sk.seges.sesam.pap.service.annotation.LocalService;
import sk.seges.sesam.pap.service.annotation.LocalServiceDelegate;

public class ServiceConverterProcessorConfigurer extends DelegateProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { LocalService.class };
		}
		return new Type[] {};
	}

	@Override
	protected Class<? extends Annotation> getDelegatedAnnotationClass() {
		return LocalServiceDelegate.class;
	}

	@Override
	protected Annotation getAnnotationFromDelegate(Annotation annotationDelegate) {
		if (annotationDelegate instanceof LocalServiceDelegate) {
			return ((LocalServiceDelegate)annotationDelegate).value();
		}
		return null;
	}

}