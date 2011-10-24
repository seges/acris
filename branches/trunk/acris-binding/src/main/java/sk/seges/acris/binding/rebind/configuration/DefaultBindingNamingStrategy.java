package sk.seges.acris.binding.rebind.configuration;

import sk.seges.acris.binding.pap.model.BeanWrapperType;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.validation.rebind.TypeStrategy;

public class DefaultBindingNamingStrategy implements BindingNamingStrategy {

	private static final String WRAPPER_IMPL_SUFFIX = "Impl";
	private static final String BINDER_IMPL_SUFFIX = "Binder";

	private TypeStrategy typeStrategy;

	public DefaultBindingNamingStrategy(TypeStrategy typeStrategy) {
		this.typeStrategy = typeStrategy;
	}

	@Override
	public String getBeanWrapperName(String beanName) {
		return beanName + BeanWrapperType.BEAN_WRAPPER_SUFFIX;
	}

	@Override
	public String getBeansBinderName(String beanName) {
		return beanName + BINDER_IMPL_SUFFIX;
	}

	@Override
	public void setGeneratorContext(GeneratorContext context) {
		typeStrategy.setGeneratorContext(context);
	}

	@Override
	public String getBeanTypeName(String typeName) {
		return typeStrategy.getBeanTypeName(typeName);
	}

	@Override
	public String getValidatorTypeName(String typeName) {
		return typeStrategy.getValidatorTypeName(typeName);
	}

	@Override
	public String getBeanWrapperImplementationName(String beanName) {
		return beanName + WRAPPER_IMPL_SUFFIX;
	}
}