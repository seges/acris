package sk.seges.sesam.core.test.selenium.configuration.api;

import sk.seges.sesam.core.configuration.api.ConfigurationValue;


public interface SeleniumConfigurator {

    ConfigurationValue[] collectSystemProperties();

    TestEnvironment mergeTestConfiguration(TestEnvironment environment);
    MailSettings mergeMailConfiguration(MailSettings mailEnvironment);
}