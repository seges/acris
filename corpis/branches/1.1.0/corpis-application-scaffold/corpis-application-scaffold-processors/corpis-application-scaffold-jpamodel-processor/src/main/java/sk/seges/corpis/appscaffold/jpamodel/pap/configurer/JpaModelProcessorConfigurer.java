package sk.seges.corpis.appscaffold.jpamodel.pap.configurer;

import java.lang.reflect.Type;

import sk.seges.corpis.appscaffold.shared.annotation.domain.JpaModel;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

public class JpaModelProcessorConfigurer extends DefaultProcessorConfigurer {
	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] {
					JpaModel.class
			};
		}
		return new Type[] {};
	}

}
