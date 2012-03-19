package sk.seges.sesam.core.test.selenium.configuration.api;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import sk.seges.sesam.core.configuration.annotation.NotSupported;
import sk.seges.sesam.core.configuration.api.IsParameter;

public enum Browsers implements IsParameter {

	FIREFOX("firefox") {
		@Override
		public WebDriver getWebDriver() {
			return new FirefoxDriver();
		}
	},
	IE("iexplore") {
		@Override
		public WebDriver getWebDriver() {
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			return new InternetExplorerDriver(ieCapabilities);
		}
	},
	@NotSupported
	SAFARI("safari") {
		@Override
		public WebDriver getWebDriver() {
			throw new RuntimeException(toString() + " driver is not supported!");
		}
	},
	@NotSupported
	OPERA("opera") {
		@Override
		public WebDriver getWebDriver() {
			throw new RuntimeException(toString() + " driver is not supported!");
		}
	},
	CHROME("chrome") {
		@Override
		public WebDriver getWebDriver() {
//			  // see flags here http://peter.sh/experiments/chromium-command-line-switches/
//		      DesiredCapabilities capabilities = DesiredCapabilities.Chrome();
//
//		      //tell chrome browser window to start maximized
//		      String[] switches = { "start-maximized" };
//		      capabilities.SetCapability("chrome.switches", switches);
//
//		      driver = new ChromeDriver(capabilities); 
		      return new ChromeDriver();
		}
	},
	HTML_UNIT("html_unit") {
		@Override
		public WebDriver getWebDriver() {
			return new HtmlUnitDriver();
		}
	};

	private String browser;

	Browsers(String browser) {
		this.browser = browser;
	}

	@Override
	public String toString() {
		return browser;
	}

	public abstract WebDriver getWebDriver();

	public static Browsers get(String name) {
		for (Browsers browser : values()) {
			if (browser.toString().contains(name)) {
				return browser;
			}
		}
		return null;
	}
}