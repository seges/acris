package sk.seges.sesam.core.test.selenium.factory;

import org.openqa.selenium.WebDriver;

import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;

public interface WebDriverFactory {

	WebDriver createSelenium(TestEnvironment testEnvironment);

}
