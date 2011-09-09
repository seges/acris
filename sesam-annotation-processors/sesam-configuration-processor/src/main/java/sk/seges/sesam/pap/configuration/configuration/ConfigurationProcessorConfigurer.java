package sk.seges.sesam.pap.configuration.configuration;

import java.lang.reflect.Type;

import javax.lang.model.element.ElementKind;

import sk.seges.sesam.core.annotation.configuration.ProcessorConfiguration;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

public class ConfigurationProcessorConfigurer extends DefaultProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { ProcessorConfiguration.class };
		}
		return super.getConfigurationElement(element);
	}

	@Override
	protected boolean isSupportedKind(ElementKind kind) {
		return kind.equals(ElementKind.ANNOTATION_TYPE);
	}
}