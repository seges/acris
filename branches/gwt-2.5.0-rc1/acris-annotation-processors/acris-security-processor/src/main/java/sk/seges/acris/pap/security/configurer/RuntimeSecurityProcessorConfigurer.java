package sk.seges.acris.pap.security.configurer;

import java.lang.reflect.Type;

import sk.seges.acris.security.client.annotations.RuntimeSecurity;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

public class RuntimeSecurityProcessorConfigurer extends DefaultProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { RuntimeSecurity.class };
		}

		return new Type[] {};
	}
}