package sk.seges.sesam.core.test.selenium.factory;

import org.openqa.selenium.WebDriver;

import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;
import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;

public class LocalWebDriverFactory implements WebDriverFactory {

	@Override
	public WebDriver createSelenium(SeleniumSettings testEnvironment) {
		Browsers browser = testEnvironment.getBrowser();
		if (browser == null) {
			throw new RuntimeException("Unknown or null browser " + testEnvironment.getBrowser());
		}
		return browser.getWebDriver();
	}
}