package sk.seges.acris.binding.jsr269;

import java.lang.annotation.Annotation;

import sk.seges.acris.binding.client.processor.IBeanPropertyConverter;

public interface BeanWrapperConfiguration {

	AnnotationDescriptor[] getAnnotations();

	ClassDescriptor[] getInterfaces();

	ClassDescriptor[] getClasses();

	public static interface ResourceDescriptor<T> {

		T getResourceType();
	}

	public static interface ResourcePropertyConverterAware {

		IBeanPropertyConverter[] getResourceConverters();
	}

	public static interface ClassDescriptor extends ResourceDescriptor<Class<?>>, ResourcePropertyConverterAware {}

	public static interface AnnotationDescriptor extends ResourceDescriptor<Class<? extends Annotation>>, ResourcePropertyConverterAware {}
}