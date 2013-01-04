package sk.seges.sesam.core.test.webdriver.factory;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;

public class RemoteWebDriverFactory implements WebDriverFactory {

	class RemoteWebDriverSupport extends RemoteWebDriver implements TakesScreenshot {

		public RemoteWebDriverSupport(URL remoteAddress, Capabilities capabilities) {
			super(remoteAddress, capabilities);
		}

		@Override
		public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
			String base64Str = execute(DriverCommand.SCREENSHOT).getValue().toString();
			return target.convertFromBase64Png(base64Str);
		}
	}
	
	@Override
	public WebDriver createSelenium(SeleniumSettings testEnvironment) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		capabilities.setBrowserName(testEnvironment.getBrowser().toString());
		try {
			return new EventFiringWebDriver(new RemoteWebDriverSupport(new URL(testEnvironment.getRemoteServerURL()), capabilities));
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