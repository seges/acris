package sk.seges.corpis.core.pap.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.lang.model.element.AnnotationMirror;

import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.configuration.DelegateDataAccessObject;
import sk.seges.sesam.core.pap.configuration.DelegateProcessorConfigurer;

public class DaoApiProcessorConfigurer extends DelegateProcessorConfigurer {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/dao-api.properties";

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] {
					DataAccessObject.class
			};
		}
		return super.getConfigurationElement(element);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends Annotation>[] getDelegatedAnnotationClasses() {
		return new Class[] { DelegateDataAccessObject.class };
	}

	@Override
	protected Annotation getAnnotationFromDelegate(Annotation annotationDelegate) {
		if (annotationDelegate instanceof DelegateDataAccessObject) {
			return ((DelegateDataAccessObject)annotationDelegate).value();
		}
		return null;
	}

	@Override
	protected AnnotationMirror getAnnotationFromDelegate(AnnotationMirror annotationDelegate) {
		return (AnnotationMirror)getAnnotationValueByReturnType(DataAccessObject.class, annotationDelegate);
	}

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}
}