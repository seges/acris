package sk.seges.sesam.model.metadata.configuration;

import sk.seges.sesam.model.metadata.configuration.MetaModelConfiguration.ResourceDescriptor;


public class DefaultResourceDescriptor<T> implements ResourceDescriptor<T> {

	private final T resource;
	
	public DefaultResourceDescriptor(T resource) {
		this.resource = resource;
	}
	
	@Override
	public T getResourceType() {
		return resource;
	}

}