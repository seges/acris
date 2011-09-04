package sk.seges.sesam.core.test.selenium.configuration;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import sk.seges.sesam.core.configuration.api.Configuration;
import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.test.selenium.configuration.api.MailSettings;
import sk.seges.sesam.core.test.selenium.configuration.api.SeleniumConfigurator;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;

public class DefaultSeleniumConfigurator implements SeleniumConfigurator {

    private static final String CONFIGURATION_PROPERTY_PREFIX = "";
    
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

    public TestEnvironment mergeTestConfiguration(TestEnvironment environment) {

    	DefaultTestEnvironment configuredEnvironment = new DefaultTestEnvironment(collectSystemProperties());
    	if (environment != null) {
    		configuredEnvironment.merge(environment);
    	}
    	
    	return configuredEnvironment;
    }

	@Override
	public MailSettings mergeMailConfiguration(MailSettings mailEnvironment) {
    	ConfigurationValue[] configurations = collectSystemProperties();

    	DefaultMailSettings configuredEnvironment = new DefaultMailSettings(configurations);
    	if (mailEnvironment != null) {
    		configuredEnvironment.merge(mailEnvironment);
    	}
    	
    	return configuredEnvironment;
	}
}