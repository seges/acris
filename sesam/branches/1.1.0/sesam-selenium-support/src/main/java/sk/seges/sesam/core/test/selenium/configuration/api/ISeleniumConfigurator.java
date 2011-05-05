package sk.seges.sesam.core.test.selenium.configuration.api;

import sk.seges.sesam.core.test.selenium.configuration.api.properties.ConfigurationValue;

public interface ISeleniumConfigurator {

    ConfigurationValue[] collectSystemProperties();

    TestEnvironment mergeConfiguration(TestEnvironment environment);

}