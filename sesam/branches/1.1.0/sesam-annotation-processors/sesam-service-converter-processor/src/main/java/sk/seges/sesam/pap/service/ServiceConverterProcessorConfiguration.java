package sk.seges.sesam.pap.service;

import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;
import sk.seges.sesam.pap.service.annotation.LocalService;

public class ServiceConverterProcessorConfiguration extends DefaultProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { LocalService.class };
		}
		return super.getConfigurationElement(element);
	}

}