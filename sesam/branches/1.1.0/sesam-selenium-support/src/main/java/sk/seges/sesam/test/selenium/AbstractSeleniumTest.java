package sk.seges.sesam.test.selenium;

import org.junit.After;
import org.junit.Before;

import sk.seges.sesam.core.test.selenium.configuration.api.ISeleniumConfigurator;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.ConfigurationValue;
import bromine.brunit.BRUnit;

public abstract class AbstractSeleniumTest extends BRUnit implements ISeleniumConfigurator {

	protected TestEnvironment testEnvironment;

	private ISeleniumConfigurator celeniumConfigurator;

	protected AbstractSeleniumTest(TestEnvironment testEnvironment) {
		this.testEnvironment = testEnvironment;
	}
	
	@Override
	public ConfigurationValue[] collectSystemProperties() {
		return celeniumConfigurator.collectSystemProperties();
	}

	@Override
	public TestEnvironment mergeConfiguration(TestEnvironment environment) {
		return celeniumConfigurator.mergeConfiguration(environment);
	}

	public void runTests() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		super.setUp(testEnvironment.getBromineEnvironment().getBromineHost(), testEnvironment.getBromineEnvironment().getBrominePort());
		super.start(testEnvironment.getSeleniumEnvironment().getSeleniumHost(), testEnvironment.getSeleniumEnvironment().getSeleniumPort(), 
				testEnvironment.getBrowser(), testEnvironment.getHost(), getClass().getSimpleName());
	}

	@After
    public void tearDown() throws Exception {
        super.tearDown();
    }

	@Override
	public void verifyTrue(Boolean statement1) throws Exception {
		if (!statement1) {
			throw new RuntimeException("Test failed");
		}
	}
	
	protected void waitAndClick(String xpath) throws Exception {
		waitForElementPresent(xpath);
		selenium.click(xpath);
	}

	protected void waitForTextPresent(String xpath) throws Exception {
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent(xpath)) break; } catch (Exception e) {}
			Thread.sleep(1000);
		} 
	}
	
	protected void waitForElementPresent(String xpath) throws Exception {
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent(xpath)) break; } catch (Exception e) {}
			Thread.sleep(1000);
		} 
	}
}
