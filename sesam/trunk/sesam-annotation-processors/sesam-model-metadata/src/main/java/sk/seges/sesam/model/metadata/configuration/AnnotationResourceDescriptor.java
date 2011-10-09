package sk.seges.sesam.model.metadata.configuration;

import java.lang.annotation.Annotation;

import sk.seges.sesam.model.metadata.configuration.MetaModelConfiguration.AnnotationDescriptor;
import sk.seges.sesam.model.metadata.strategy.api.ModelPropertyConverter;


public class AnnotationResourceDescriptor extends AbstractPropertyConverterAwareDescriptor<Class<? extends Annotation>> implements AnnotationDescriptor {

	public AnnotationResourceDescriptor(Class<? extends Annotation> t, ModelPropertyConverter... propertyConverters) {
		super(t, propertyConverters);
	}
}