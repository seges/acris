package sk.seges.acris.theme.pap.configurer;

import java.lang.reflect.Type;

import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

public class ThemeProcessorConfigurer extends DefaultProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] {
					ThemeSupport.class
			};
		}
		return new Type[] {};
	}

}
