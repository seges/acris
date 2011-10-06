package sk.seges.corpis.appscaffold.datainterface.pap.configurer;

import java.lang.reflect.Type;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

public class DataInterfaceProcessorConfigurer extends DefaultProcessorConfigurer {
	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] {
					DomainInterface.class
			};
		}
		return new Type[] {};
	}

}
