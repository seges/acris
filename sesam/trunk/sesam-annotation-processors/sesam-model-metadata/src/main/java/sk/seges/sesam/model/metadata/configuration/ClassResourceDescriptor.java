package sk.seges.sesam.model.metadata.configuration;

import sk.seges.sesam.model.metadata.configuration.MetaModelConfiguration.ClassDescriptor;
import sk.seges.sesam.model.metadata.strategy.api.ModelPropertyConverter;


public class ClassResourceDescriptor extends AbstractPropertyConverterAwareDescriptor<Class<?>> implements ClassDescriptor {

	public ClassResourceDescriptor(Class<?> t, ModelPropertyConverter... propertyConverters) {
		super(t, propertyConverters);
	}
}