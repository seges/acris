package sk.seges.acris.binding.jsr269.configuration;

import sk.seges.acris.binding.client.processor.IBeanPropertyConverter;
import sk.seges.acris.binding.jsr269.BeanWrapperConfiguration.ClassDescriptor;


public class ClassResourceDescriptor extends AbstractPropertyConverterAwareDescriptor<Class<?>> implements ClassDescriptor {

	public ClassResourceDescriptor(Class<?> t, IBeanPropertyConverter... propertyConverters) {
		super(t, propertyConverters);
	}
}