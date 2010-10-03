package sk.seges.acris.binding.jsr269.configuration;

import java.lang.annotation.Annotation;

import sk.seges.acris.binding.client.processor.IBeanPropertyConverter;
import sk.seges.acris.binding.jsr269.BeanWrapperConfiguration.AnnotationDescriptor;


public class AnnotationResourceDescriptor extends AbstractPropertyConverterAwareDescriptor<Class<? extends Annotation>> implements AnnotationDescriptor {

	public AnnotationResourceDescriptor(Class<? extends Annotation> t, IBeanPropertyConverter... propertyConverters) {
		super(t, propertyConverters);
	}
}