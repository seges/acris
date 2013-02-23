package sk.seges.corpis.core.pap.dao.configurer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DelegateDataAccessObject;
import sk.seges.sesam.core.pap.configuration.DelegateProcessorConfigurer;

public class AbstractHibernateDaoProcessorConfigurer extends
		DelegateProcessorConfigurer {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/dao-hibernate.properties";

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { DataAccessObject.class };
		}
		return new Type[] {};
	}

	@Override
	protected Class<? extends Annotation> getDelegatedAnnotationClass() {
		return DelegateDataAccessObject.class;
	}

	@Override
	protected Annotation getAnnotationFromDelegate(Annotation annotationDelegate) {
		if (annotationDelegate instanceof DelegateDataAccessObject) {
			return ((DelegateDataAccessObject) annotationDelegate).value();
		}
		return null;
	}

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}
}
