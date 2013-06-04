package sk.seges.sesam.model.metadata.configuration;

import sk.seges.sesam.model.metadata.configuration.MetaModelConfiguration.ResourcePropertyConverterAware;
import sk.seges.sesam.model.metadata.strategy.api.ModelPropertyConverter;


public class AbstractPropertyConverterAwareDescriptor<T> extends DefaultResourceDescriptor<T> implements ResourcePropertyConverterAware {

	private final ModelPropertyConverter[] propertyConverters;
	
	public AbstractPropertyConverterAwareDescriptor(T t, ModelPropertyConverter... propertyConverters) {
		super(t);
		this.propertyConverters = propertyConverters;
	}

	@Override
	public ModelPropertyConverter[] getResourceConverters() {
		return propertyConverters;
	}
}