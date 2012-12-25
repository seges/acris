/**
   Copyright 2011 Seges s.r.o.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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

	public static Boolean[] getConfigurationBooleans(ConfigurationValue[] configurations, String key) {
		String[] configurationValues = getConfigurationValues(configurations, key);
		
		Boolean[] result = new Boolean[configurationValues.length];
		
		for (int i = 0; i < configurationValues.length; i++) {
			try {
				result[i] = Boolean.getBoolean(configurationValues[i]);
			} catch (Exception e) {
				return new Boolean[0];
			}
		}
		
		return result;
	}
	
	private static final String VALUES_DELIMITER = ",";
	
	public static String[] getConfigurationValues(ConfigurationValue[] configurations, String key) {
		if (configurations == null) {
			return null;
		}
		for (ConfigurationValue configuration: configurations) {
			if (configuration.getConfiguration().getKey().equals(key)) {
				return configuration.getValue().split(VALUES_DELIMITER);
			}
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

	public static int[] getConfigurationInts(ConfigurationValue[] configurations, String key) {
		String[] configurationValues = getConfigurationValues(configurations, key);
		
		int[] result = new int[configurationValues.length];
		
		for (int i = 0; i < configurationValues.length; i++) {
			try {
				result[i] = Integer.getInteger(configurationValues[i]).intValue();
			} catch (Exception e) {
				return new int[0];
			}
		}
		
		return result;
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

	public static short[] getConfigurationShorts(ConfigurationValue[] configurations, String key) {
		String[] configurationValues = getConfigurationValues(configurations, key);
		
		short[] result = new short[configurationValues.length];
		
		for (int i = 0; i < configurationValues.length; i++) {
			try {
				result[i] = Short.parseShort(configurationValues[i]);
			} catch (Exception e) {
				return new short[0];
			}
		}
		
		return result;
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

	public static byte[] getConfigurationBytes(ConfigurationValue[] configurations, String key) {
		String[] configurationValues = getConfigurationValues(configurations, key);
		
		byte[] result = new byte[configurationValues.length];
		
		for (int i = 0; i < configurationValues.length; i++) {
			try {
				result[i] = Byte.parseByte(configurationValues[i]);
			} catch (Exception e) {
				return new byte[0];
			}
		}
		
		return result;
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

	public static char[] getConfigurationChars(ConfigurationValue[] configurations, String key) {
		String[] configurationValues = getConfigurationValues(configurations, key);
		
		char[] result = new char[configurationValues.length];
		
		for (int i = 0; i < configurationValues.length; i++) {
			result[i] = configurationValues[i].charAt(0);
		}
		
		return result;
	}

	public static char getConfigurationChar(ConfigurationValue[] configurations, Configuration config) {
		String configurationValue = getConfigurationValue(configurations, config);
		if (configurationValue == null || configurationValue.length() == 0) {
			return 0;
		}
		return configurationValue.charAt(0);
	}

	public static Long[] getConfigurationLongs(ConfigurationValue[] configurations, String key) {
		String[] configurationValues = getConfigurationValues(configurations, key);
		
		Long[] result = new Long[configurationValues.length];
		
		for (int i = 0; i < configurationValues.length; i++) {
			try {
				result[i] = Long.getLong(configurationValues[i]).longValue();
			} catch (Exception e) {
				return new Long[0];
			}
		}
		
		return result;
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

	public static Float[] getConfigurationFloats(ConfigurationValue[] configurations, String key) {
		String[] configurationValues = getConfigurationValues(configurations, key);
		
		Float[] result = new Float[configurationValues.length];
		
		for (int i = 0; i < configurationValues.length; i++) {
			try {
				result[i] = Float.valueOf(configurationValues[i]);
			} catch (Exception e) {
				return new Float[0];
			}
		}
		
		return result;
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

	public static Double[] getConfigurationDoubles(ConfigurationValue[] configurations, String key) {
		String[] configurationValues = getConfigurationValues(configurations, key);
		
		Double[] result = new Double[configurationValues.length];
		
		for (int i = 0; i < configurationValues.length; i++) {
			try {
				result[i] = Double.valueOf(configurationValues[i]);
			} catch (Exception e) {
				return new Double[0];
			}
		}
		
		return result;
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