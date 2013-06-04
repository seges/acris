package sk.seges.sesam.core.test.selenium.support.api;

public interface SeleniumSupport {

	void waitForAlertPresent();
	void waitForAlert(String pattern);

	void waitForTextPresent(String xpath);
	void waitForElementPresent(String xpath);

	void waitForTextsPresent(String...xpaths);
	void waitForElementsPresent(String...xpaths);

	void waitAndClick(String xpath);

	void fail(String message);
	
	void clickOnElement(String xpath);

	String getRandomString(int length);
}