package sk.seges.sesam.core.test.selenium.runner;

import sk.seges.sesam.core.test.selenium.configuration.DefaultSeleniumConfigurator;
import sk.seges.sesam.core.test.selenium.configuration.api.MailSettings;
import sk.seges.sesam.core.test.selenium.configuration.api.SeleniumConfigurator;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.ConfigurationValue;
import sk.seges.sesam.test.selenium.AbstractSeleniumTest;

public class SeleniumSuiteRunner implements SeleniumConfigurator {

	private SeleniumConfigurator seleniumConfigurator;
	
	protected SeleniumSuiteRunner() {
		seleniumConfigurator = new DefaultSeleniumConfigurator();
	}
	
    public void run(AbstractSeleniumTest test) throws Exception {
    	test.setUp();
       	test.runTests();
        test.tearDown();
    }

	@Override
	public ConfigurationValue[] collectSystemProperties() {
		return seleniumConfigurator.collectSystemProperties();
	}

	@Override
	public TestEnvironment mergeTestConfiguration(TestEnvironment environment) {
		return seleniumConfigurator.mergeTestConfiguration(environment);
	}

	@Override
	public MailSettings mergeMailConfiguration(MailSettings mailEnvironment) {
		return seleniumConfigurator.mergeMailConfiguration(mailEnvironment);
	}
}