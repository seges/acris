package sk.seges.sesam.pap.metadata.configurer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.configuration.DelegateProcessorConfigurer;
import sk.seges.sesam.model.metadata.annotation.MetaModel;
import sk.seges.sesam.model.metadata.annotation.configuration.MetaModelDelegate;

public class MetaModelProcessorConfigurer extends DelegateProcessorConfigurer {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/meta-model.properties";

	@Override
	protected Class<? extends Annotation> getDelegatedAnnotationClass() {
		return MetaModelDelegate.class;
	}

	@Override
	protected Annotation getAnnotationFromDelegate(Annotation annotationDelegate) {
		if (annotationDelegate instanceof MetaModelDelegate) {
			return ((MetaModelDelegate)annotationDelegate).value();
		}
		
		return null;
	}

	@Override
	protected String getConfigurationFileName() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}
	
	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] {
				MetaModel.class
			};
		}
		return new Type[] {};
	}
}