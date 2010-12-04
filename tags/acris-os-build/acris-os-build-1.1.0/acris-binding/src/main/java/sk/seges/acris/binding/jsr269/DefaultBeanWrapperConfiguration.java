package sk.seges.acris.binding.jsr269;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.binding.jsr269.configuration.AnnotationResourceDescriptor;
import sk.seges.acris.binding.jsr269.configuration.ClassResourceDescriptor;
import sk.seges.sesam.domain.IDomainObject;


public class DefaultBeanWrapperConfiguration implements BeanWrapperConfiguration {

	@Override
	public AnnotationDescriptor[] getAnnotations() {
		return new AnnotationDescriptor[] {
				new AnnotationResourceDescriptor(BeanWrapper.class)
		};
	}

	@Override
	public ClassDescriptor[] getInterfaces() {
		return new ClassDescriptor[] {
				new ClassResourceDescriptor(IDomainObject.class)
		};
	}

	@Override
	public ClassDescriptor[] getClasses() {
		return new ClassDescriptor[] {};
	}
}