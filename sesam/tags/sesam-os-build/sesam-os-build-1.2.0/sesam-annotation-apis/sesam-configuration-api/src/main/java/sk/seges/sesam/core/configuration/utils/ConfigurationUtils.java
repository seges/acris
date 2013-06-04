package sk.seges.sesam.core.configuration.utils;

import sk.seges.sesam.core.configuration.api.Configuration;
import sk.seges.sesam.core.configuration.api.ConfigurationValue;

public class ConfigurationUtils {

	public static Boolean getConfigurationBoolean(ConfigurationValue[] configurations, String key) {
		String configurationValue = getConfigurationValue(configurations, key);
		if (configurationValue == null) {
			return null;
		}
		try {
			return Boolean.getBoolean(configurationValue);
		} catch (Exception e) {
		}
		return null;
	}

	public static Boolean getConfigurationBoolean(ConfigurationValue[] configurations, Configuration config) {
		String configurationValue = getConfigurationValue(configurations, config);
		if (configurationValue == null) {
			return null;
		}
		try {
			return Boolean.getBoolean(configurationValue);
		} catch (Exception e) {
		}
		return null;
	}

	public static String getConfigurationValue(ConfigurationValue[] configurations, String key) {
		if (configurations == null) {
			return null;
		}
		for (ConfigurationValue configuration: configurations) {
			if (configuration.getConfiguration().getKey().equals(key)) {
				return configuration.getValue();
			}
		}
		
		return null;
	}
	
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

	public static int getConfigurationInt(ConfigurationValue[] configurations, String key) {
		String configurationValue = getConfigurationValue(configurations, key);
		if (configurationValue == null) {
			return 0;
		}
		try {
			return Integer.getInteger(configurationValue).intValue();
		} catch (Exception e) {
		}
		return 0;
	}

	public static int getConfigurationInt(ConfigurationValue[] configurations, Configuration config) {
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

	public static short getConfigurationShort(ConfigurationValue[] configurations, Configuration config) {
		String configurationValue = getConfigurationValue(configurations, config);
		if (configurationValue == null) {
			return 0;
		}
		try {
			return Short.parseShort(configurationValue);
		} catch (Exception e) {
		}
		return 0;
	}

	public static byte getConfigurationByte(ConfigurationValue[] configurations, Configuration config) {
		String configurationValue = getConfigurationValue(configurations, config);
		if (configurationValue == null) {
			return 0;
		}
		try {
			return Byte.parseByte(configurationValue);
		} catch (Exception e) {
		}
		return 0;
	}

	public static char getConfigurationChar(ConfigurationValue[] configurations, Configuration config) {
		String configurationValue = getConfigurationValue(configurations, config);
		if (configurationValue == null || configurationValue.length() == 0) {
			return 0;
		}
		return configurationValue.charAt(0);
	}

	public static Long getConfigurationLong(ConfigurationValue[] configurations, String key) {
		String configurationValue = getConfigurationValue(configurations, key);
		if (configurationValue == null) {
			return 0L;
		}
		try {
			return Long.getLong(configurationValue).longValue();
		} catch (Exception e) {
		}
		return 0L;
	}

	public static Float getConfigurationFloat(ConfigurationValue[] configurations, String key) {
		String configurationValue = getConfigurationValue(configurations, key);
		if (configurationValue == null) {
			return 0F;
		}
		try {
			return Float.valueOf(configurationValue);
		} catch (Exception e) {
		}
		return 0F;
	}

	public static Double getConfigurationDouble(ConfigurationValue[] configurations, String key) {
		String configurationValue = getConfigurationValue(configurations, key);
		if (configurationValue == null) {
			return 0.0;
		}
		try {
			return Double.valueOf(configurationValue);
		} catch (Exception e) {
		}
		return 0.0;
	}
}