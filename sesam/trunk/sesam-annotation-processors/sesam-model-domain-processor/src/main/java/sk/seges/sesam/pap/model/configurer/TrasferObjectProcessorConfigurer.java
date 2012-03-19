package sk.seges.sesam.pap.model.configurer;

import java.lang.reflect.Type;

import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

public class TrasferObjectProcessorConfigurer extends DefaultProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
				return new Type[] {
						TransferObjectMapping.class
				};
		}
		
		return new Type [] {};
	}

}
