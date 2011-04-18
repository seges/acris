package sk.seges.sesam.test.selenium;

import bromine.brunit.BRUnit;

public abstract class AbstractSeleniumTest extends BRUnit {

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
