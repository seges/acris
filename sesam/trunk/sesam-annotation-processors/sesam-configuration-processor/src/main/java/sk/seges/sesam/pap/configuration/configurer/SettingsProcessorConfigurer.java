package sk.seges.sesam.pap.configuration.configurer;

import java.lang.reflect.Type;

import javax.lang.model.element.ElementKind;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

public class SettingsProcessorConfigurer extends DefaultProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { Configuration.class };
		}
		return new Type [] {};
	}

	@Override
	protected boolean isSupportedKind(ElementKind kind) {
		return kind.equals(ElementKind.ANNOTATION_TYPE);
	}
}