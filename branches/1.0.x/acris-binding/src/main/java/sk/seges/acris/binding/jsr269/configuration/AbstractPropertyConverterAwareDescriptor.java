package sk.seges.acris.binding.jsr269.configuration;

import sk.seges.acris.binding.client.processor.IBeanPropertyConverter;
import sk.seges.acris.binding.jsr269.BeanWrapperConfiguration.ResourcePropertyConverterAware;


public class AbstractPropertyConverterAwareDescriptor<T> extends DefaultResourceDescriptor<T> implements ResourcePropertyConverterAware {

	private final IBeanPropertyConverter[] propertyConverters;
	
	public AbstractPropertyConverterAwareDescriptor(T t, IBeanPropertyConverter... propertyConverters) {
		super(t);
		this.propertyConverters = propertyConverters;
	}

	@Override
	public IBeanPropertyConverter[] getResourceConverters() {
		return propertyConverters;
	}
}