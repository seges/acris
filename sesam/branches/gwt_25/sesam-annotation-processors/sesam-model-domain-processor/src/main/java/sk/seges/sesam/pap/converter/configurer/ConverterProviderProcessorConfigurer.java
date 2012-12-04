package sk.seges.sesam.pap.converter.configurer;

import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

public class ConverterProviderProcessorConfigurer extends DefaultProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { TransferObjectMapping.class };
		}
		return new Type[] {};
	}	
}