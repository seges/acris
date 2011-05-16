package sk.seges.sesam.core.test.selenium.factory;

import org.openqa.selenium.WebDriver;

import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;

public class LocalWebDriverFactory implements WebDriverFactory {

	@Override
	public WebDriver createSelenium(TestEnvironment testEnvironment) {
		Browsers browser = Browsers.get(testEnvironment.getBrowser());
		if (browser == null) {
			throw new RuntimeException("Unknown or null browser " + testEnvironment.getBrowser());
		}
		return browser.getWebDriver();
	}
}