package sk.seges.sesam.model.metadata.configuration;

import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;



public class DefaultMetaModelConfiguration implements MetaModelConfiguration {

	@Override
	public AnnotationDescriptor[] getAnnotations() {
		return new AnnotationDescriptor[] {
				new AnnotationResourceDescriptor(MetaModel.class)
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