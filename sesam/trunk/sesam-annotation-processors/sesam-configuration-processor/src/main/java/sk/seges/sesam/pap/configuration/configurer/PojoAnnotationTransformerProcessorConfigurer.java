package sk.seges.sesam.pap.configuration.configurer;

import java.lang.reflect.Type;

import javax.lang.model.element.ElementKind;

import sk.seges.sesam.core.configuration.annotation.GenerateModel;
import sk.seges.sesam.core.pap.configuration.DefaultProcessorConfigurer;

public class PojoAnnotationTransformerProcessorConfigurer extends DefaultProcessorConfigurer {

	@Override
	public boolean isSupportedKind(ElementKind kind) {
		return kind.equals(ElementKind.ANNOTATION_TYPE);
	}
	
	@Override
	protected Type[] getConfigurationElement(DefaultConfigurationElement element) {
		switch (element) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { GenerateModel.class };
		}
		return new Type[] {};
	}

}
