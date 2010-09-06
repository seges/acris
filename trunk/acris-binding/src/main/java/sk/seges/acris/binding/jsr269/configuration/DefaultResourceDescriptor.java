package sk.seges.acris.binding.jsr269.configuration;

import sk.seges.acris.binding.jsr269.BeanWrapperConfiguration.ResourceDescriptor;


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