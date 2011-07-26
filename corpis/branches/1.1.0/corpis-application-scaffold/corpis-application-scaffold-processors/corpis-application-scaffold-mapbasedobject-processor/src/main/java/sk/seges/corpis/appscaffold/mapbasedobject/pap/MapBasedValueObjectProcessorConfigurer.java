package sk.seges.corpis.appscaffold.mapbasedobject.pap;

import java.lang.reflect.Type;

import sk.seges.corpis.appscaffold.shared.annotation.MapBased;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

public class MapBasedValueObjectProcessorConfigurer extends DefaultProcessorConfigurer {

	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { MapBased.class };
		}
		return super.getConfigurationElement(element);
	}
}