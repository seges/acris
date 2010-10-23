package sk.seges.acris.generator.server.spring;

import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractRefreshableApplicationContext;

import sk.seges.sesam.spring.config.AdvancedPropertyPlaceholderConfigurer;

public class SpringBeanPropertyRegister extends AdvancedPropertyPlaceholderConfigurer implements BeanFactoryPostProcessor, ApplicationContextAware {

	private Properties properties;
	
	private ApplicationContext context;
	
	public SpringBeanPropertyRegister() {
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		properties = props;
	}
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		super.postProcessBeanFactory(beanFactory);
		
		if (properties == null) {
			return;
		}
		
		for (Entry<Object, Object> property : properties.entrySet()) {
			String beanValue = (String)property.getValue();
			
			try {
				Integer value = Integer.parseInt(beanValue);
				registerProperty((String)property.getKey(), value, Integer.class);
				continue;
			} catch (NumberFormatException nbf) {
				//Nothing happen ... we are going to check other types
			}

			try {
				Double value = Double.parseDouble(beanValue);
				registerProperty((String)property.getKey(), value, Double.class);
				continue;
			} catch (NumberFormatException nbf) {
				//Nothing happen ... we are going to check other types
			}

			if ("true".equals(beanValue.toLowerCase())) {
				registerProperty((String)property.getKey(), new Boolean(true), Boolean.class);
			} else if ("false".equals(beanValue.toLowerCase())) {
				registerProperty((String)property.getKey(), new Boolean(false), Boolean.class);
			} else {
				registerProperty((String)property.getKey(), (String)property.getValue(), String.class);
			}
		}
	}

	private boolean registerProperty(String property, Object value, Class<?> clazz) {
		RootBeanDefinition rbd = new RootBeanDefinition();

		rbd.setAbstract(false);
		rbd.setLazyInit(true);
		rbd.setAutowireCandidate(true);

		ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
		rbd.setConstructorArgumentValues(constructorArgumentValues);

		try {

			constructorArgumentValues.addIndexedArgumentValue(0, value);

			rbd.setBeanClass(clazz);
 
			if (context instanceof AbstractRefreshableApplicationContext) {
				AbstractRefreshableApplicationContext refreshableContext = (AbstractRefreshableApplicationContext)context;
				DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)refreshableContext.getBeanFactory();
				beanFactory.registerBeanDefinition(property, rbd);
			} else {
				BeanDefinitionRegistry registry = ((BeanDefinitionRegistry)context);
				registry.registerBeanDefinition(property, rbd);				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}