package sk.seges.sesam.core.test.webdriver.factory;

import java.net.URL;

import org.openqa.selenium.SeleneseCommandExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;

public class RemoteWebDriverFactory implements WebDriverFactory {

	@Override
	public WebDriver createSelenium(SeleniumSettings testEnvironment) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setBrowserName(testEnvironment.getBrowser().toString());
		CommandExecutor executor;
		try {
			executor = new SeleneseCommandExecutor(
					new URL("http", extractHost(testEnvironment.getSeleniumServer()),testEnvironment.getSeleniumPort(), "/"), 
					new URL("http", extractHost(testEnvironment.getTestURL()), 80, "/"), capabilities);
		} catch (Exception ex) {
			throw new RuntimeException("Invalid test environment specified.", ex);
		}
		return new RemoteWebDriver(executor, capabilities);
	}
	
	protected String extractHost(String host) {
		if (host.toLowerCase().startsWith("http://")) {
			return host.substring("http://".length());
		}
		if (host.toLowerCase().startsWith("https://")) {
			return host.substring("https://".length());
		}
		return host;
	}
}