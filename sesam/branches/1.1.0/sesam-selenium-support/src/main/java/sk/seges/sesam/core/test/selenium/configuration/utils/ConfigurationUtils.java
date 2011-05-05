package sk.seges.sesam.core.test.selenium.configuration.utils;

import sk.seges.sesam.core.test.selenium.configuration.api.properties.Configuration;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.ConfigurationValue;

public class ConfigurationUtils {

	public static String getConfigurationValue(ConfigurationValue[] configurations, Configuration config) {
		if (configurations == null) {
			return null;
		}
		for (ConfigurationValue configuration: configurations) {
			if (configuration.getConfiguration().getKey().equals(config.getKey())) {
				return configuration.getValue();
			}
		}
		
		return null;
	}

	public static int getConfigurationNumber(ConfigurationValue[] configurations, Configuration config) {
		String configurationValue = getConfigurationValue(configurations, config);
		if (configurationValue == null) {
			return 0;
		}
		try {
			return Integer.getInteger(configurationValue).intValue();
		} catch (Exception e) {
		}
		return 0;
	}
}
