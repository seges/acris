package sk.seges.acris.pap.service.configurer;

import java.lang.reflect.Type;

import sk.seges.acris.core.client.annotation.RemoteServicePath;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

public class AsyncServiceProcessorConfigurer extends DefaultProcessorConfigurer {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/async-service.properties";

	@Override
	protected String getConfigurationFileName() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
			case PROCESSING_ANNOTATIONS:
				return new Type[] { RemoteServiceRelativePath.class, RemoteServicePath.class, RemoteServiceDefinition.class };
		}
		return new Type[] {};
	}
}