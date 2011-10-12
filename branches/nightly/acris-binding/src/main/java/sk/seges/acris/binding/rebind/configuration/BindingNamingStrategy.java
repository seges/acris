package sk.seges.acris.binding.rebind.configuration;

import com.google.gwt.validation.rebind.TypeStrategy;

public interface BindingNamingStrategy extends TypeStrategy {

	String getBeanWrapperName(String beanName);

	String getBeanWrapperImplementationName(String beanName);

	String getBeansBinderName(String beanName);
}
