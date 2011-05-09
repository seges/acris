package sk.seges.sesam.core.test.selenium.support.api;

public interface SeleniumSupport {

	void waitForAlertPresent();
	void waitForAlert(String pattern);

	void waitForTextPresent(String xpath);
	void waitForElementPresent(String xpath);

	void fail(String message);
}