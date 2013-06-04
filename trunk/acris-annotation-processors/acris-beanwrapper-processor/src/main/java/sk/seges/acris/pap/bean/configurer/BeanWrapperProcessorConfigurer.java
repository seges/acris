package sk.seges.acris.pap.bean.configurer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.acris.core.client.annotation.BeanWrapperDelegate;
import sk.seges.sesam.core.pap.configuration.DelegateProcessorConfigurer;

public class BeanWrapperProcessorConfigurer extends DelegateProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
			case PROCESSING_ANNOTATIONS:
				return new Type[] { BeanWrapper.class };
		}
		
		return new Type[] {};
	}
	
	@Override
	protected Class<? extends Annotation> getDelegatedAnnotationClass() {
		return BeanWrapperDelegate.class;
	}

	@Override
	protected Annotation getAnnotationFromDelegate(Annotation annotationDelegate) {
		if (annotationDelegate instanceof BeanWrapperDelegate) {
			return ((BeanWrapperDelegate)annotationDelegate).value();
		}
		return null;
	}
}