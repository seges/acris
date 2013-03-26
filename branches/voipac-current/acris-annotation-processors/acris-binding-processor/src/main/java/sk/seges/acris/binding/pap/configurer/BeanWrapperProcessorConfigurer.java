/**
 * 
 */
package sk.seges.acris.binding.pap.configurer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.binding.client.annotations.configuration.BeanWrapperDelegate;
import sk.seges.sesam.core.pap.configuration.DelegateProcessorConfigurer;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;

public class BeanWrapperProcessorConfigurer extends DelegateProcessorConfigurer {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/bean-wrapper.properties";

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
			case PROCESSING_ANNOTATIONS:
				return new Type[] { BeanWrapper.class };
		}
		return new Type[] {};
	}
	
	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
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