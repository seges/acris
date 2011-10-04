package sk.seges.sesam.pap.configuration.configurer;

import java.lang.reflect.Type;

import sk.seges.sesam.core.configuration.annotation.SettingsProvider;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

public class ConfigurationProcessorProviderConfigurer extends DefaultProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { SettingsProvider.class };
		}
		return new Type[] {};
	}
}