package sk.seges.sesam.core.test.selenium.runner;

import sk.seges.sesam.core.test.selenium.configuration.SeleniumConfigurator;
import sk.seges.sesam.core.test.selenium.configuration.api.ISeleniumConfigurator;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.ConfigurationValue;
import sk.seges.sesam.test.selenium.AbstractSeleniumTest;

public class SeleniumSuiteRunner implements ISeleniumConfigurator {

	private ISeleniumConfigurator seleniumConfigurator;
	
	protected SeleniumSuiteRunner() {
		seleniumConfigurator = new SeleniumConfigurator();
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
	public TestEnvironment mergeConfiguration(TestEnvironment environment) {
		return seleniumConfigurator.mergeConfiguration(environment);
	}
}