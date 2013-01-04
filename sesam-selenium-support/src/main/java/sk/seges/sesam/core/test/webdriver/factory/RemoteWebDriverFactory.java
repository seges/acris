package sk.seges.sesam.core.test.webdriver.factory;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;

public class RemoteWebDriverFactory implements WebDriverFactory {

	@Override
	public WebDriver createSelenium(SeleniumSettings testEnvironment) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		capabilities.setBrowserName(testEnvironment.getBrowser().toString());
		try {
			return new RemoteWebDriver(new URL(testEnvironment.getRemoteServerURL()), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
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