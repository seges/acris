/**
 * 
 */
package sk.seges.acris.binding.jsr269;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.lang.model.element.AnnotationMirror;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.binding.client.annotations.configuration.BeanWrapperDelegate;
import sk.seges.sesam.core.pap.configuration.DelegateProcessorConfigurer;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;

public class BeanWrapperProcessorConfiguration extends DelegateProcessorConfigurer {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/bean-wrapper.properties";

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
			case PROCESSING_ANNOTATIONS:
				return new Type[] { BeanWrapper.class };
		}
		return super.getConfigurationElement(element);
	}
	
	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends Annotation>[] getDelegatedAnnotationClasses() {
		return new Class[] {
				BeanWrapperDelegate.class
		};
	}

	@Override
	protected Annotation getAnnotationFromDelegate(Annotation annotationDelegate) {
		if (annotationDelegate instanceof BeanWrapperDelegate) {
			return ((BeanWrapperDelegate)annotationDelegate).value();
		}
		return null;
	}

	@Override
	protected AnnotationMirror getAnnotationFromDelegate(AnnotationMirror annotationDelegate) {
		return (AnnotationMirror)getAnnotationValueByReturnType(BeanWrapper.class, annotationDelegate);
	}
}