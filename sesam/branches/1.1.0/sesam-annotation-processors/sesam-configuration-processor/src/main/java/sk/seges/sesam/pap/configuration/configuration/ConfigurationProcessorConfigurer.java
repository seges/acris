package sk.seges.sesam.pap.configuration.configuration;

import java.lang.reflect.Type;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

public class ConfigurationProcessorConfigurer extends DefaultProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { Configuration.class };
		}
		return super.getConfigurationElement(element);
	}
}
