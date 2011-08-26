package sk.seges.acris.json.jsr269;

import java.lang.reflect.Type;

import sk.seges.acris.json.client.annotation.JsonObject;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

public class JsonProcessorConfiguration extends DefaultProcessorConfigurer {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/json.properties";

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
			case PROCESSING_ANNOTATIONS:
				return new Type[] { JsonObject.class };
		}
		return super.getConfigurationElement(element);
	}
}