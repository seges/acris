package sk.seges.sesam.core.test.selenium.runner;

import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.DefaultBromineEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.DefaultSeleniumConfigurator;
import sk.seges.sesam.core.test.selenium.configuration.DefaultSeleniumEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.DefaultTestEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.BromineEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.MailSettings;
import sk.seges.sesam.core.test.selenium.configuration.api.SeleniumConfigurator;
import sk.seges.sesam.core.test.selenium.configuration.api.SeleniumEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;

public class SeleniumSuiteRunner implements SeleniumConfigurator {

	private SeleniumConfigurator seleniumConfigurator;
	
	protected SeleniumSuiteRunner() {
		seleniumConfigurator = new DefaultSeleniumConfigurator();
	}
	
    public void run(AbstractSeleniumTest test) {
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
	
	public TestEnvironment parseTestConfiguration(String[] args) {
		if (args.length < 4) {
			throw new RuntimeException("Insufficient count of the arguments. There should 4 arguments passed in the args.");
		}
		
		BromineEnvironment bromineEnvironment = new DefaultBromineEnvironment(args[0], Integer.parseInt(args[1]), true);
		SeleniumEnvironment seleniumEnvironment = new DefaultSeleniumEnvironment(collectSystemProperties());
		return new DefaultTestEnvironment(seleniumEnvironment, bromineEnvironment, args[3], "", args[2], true);
	}        
}