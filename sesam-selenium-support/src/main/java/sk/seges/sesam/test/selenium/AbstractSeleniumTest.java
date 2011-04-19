package sk.seges.sesam.test.selenium;

import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import bromine.brunit.BRUnit;

public abstract class AbstractSeleniumTest extends BRUnit {

	protected TestEnvironment testEnvironment;
	
	
	protected AbstractSeleniumTest(TestEnvironment testEnvironment) {
		this.testEnvironment = testEnvironment;
	}

	public void runTests() throws Exception {
	}

	public void setUp() throws Exception {
		super.setUp(testEnvironment.getBromineEnvironment().getBromineHost(), testEnvironment.getBromineEnvironment().getBrominePort());
		super.start(testEnvironment.getSeleniumEnvironment().getSeleniumHost(), testEnvironment.getSeleniumEnvironment().getSeleniumPort(), 
				testEnvironment.getBrowser(), testEnvironment.getTestHost(), getClass().getSimpleName());
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
