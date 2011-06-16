package sk.seges.sesam.model.metadata.configuration;

import java.lang.annotation.Annotation;

import sk.seges.sesam.model.metadata.strategy.api.ModelPropertyConverter;

public interface MetaModelConfiguration {

	AnnotationDescriptor[] getAnnotations();

	ClassDescriptor[] getInterfaces();

	ClassDescriptor[] getClasses();

	public static interface ResourceDescriptor<T> {

		T getResourceType();
	}

	public static interface ResourcePropertyConverterAware {

		ModelPropertyConverter[] getResourceConverters();
	}

	public static interface ClassDescriptor extends ResourceDescriptor<Class<?>>, ResourcePropertyConverterAware {}

	public static interface AnnotationDescriptor extends ResourceDescriptor<Class<? extends Annotation>>, ResourcePropertyConverterAware {}
}