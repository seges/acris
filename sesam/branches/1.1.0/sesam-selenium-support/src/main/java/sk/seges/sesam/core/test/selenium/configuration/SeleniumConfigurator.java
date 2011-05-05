package sk.seges.sesam.core.test.selenium.configuration;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import sk.seges.sesam.core.test.selenium.configuration.api.ISeleniumConfigurator;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.Configuration;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.ConfigurationValue;

public class SeleniumConfigurator implements ISeleniumConfigurator {

    public TestEnvironment mergeConfiguration(TestEnvironment environment) {

    	ConfigurationValue[] configurations = collectSystemProperties();

    	DefaultTestEnvironment configuredEnvironment = new DefaultTestEnvironment(configurations);
    	if (environment != null) {
    		configuredEnvironment.merge(environment);
    	}
    	
    	return configuredEnvironment;
    }

    private static final String CONFIGURATION_PROPERTY_PREFIX = "test.";
    
    public ConfigurationValue[] collectSystemProperties() {
    	Properties properties = System.getProperties();
    	if (properties == null) {
    		return new ConfigurationValue[] {};
    	}
    	
    	Set<ConfigurationValue> configurations = new HashSet<ConfigurationValue>();

    	Enumeration<Object> keys = properties.keys();
    	while (keys.hasMoreElements()) {
    		final Object key = keys.nextElement();
    		if (key.toString().startsWith(CONFIGURATION_PROPERTY_PREFIX)) {
	    		final String property = properties.getProperty(key.toString());
	    		if (property != null) {
	    			configurations.add(new ConfigurationValue() {
	    				
						@Override
						public String getValue() {
							return property;
						}
						
						@Override
						public Configuration getConfiguration() {
							return new Configuration() {
								
								@Override
								public String getKey() {
									return key.toString();
								}
							};
						}
					});
	    		}
    		}
    	}
    	
    	return configurations.toArray(new ConfigurationValue[] {});
    }

}
