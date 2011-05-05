package sk.seges.sesam.core.test.selenium.configuration;

import sk.seges.sesam.core.test.selenium.configuration.api.BromineEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;
import sk.seges.sesam.core.test.selenium.configuration.api.SeleniumEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.Configuration;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.ConfigurationValue;
import sk.seges.sesam.core.test.selenium.configuration.utils.ConfigurationUtils;

public class DefaultTestEnvironment implements TestEnvironment {

	public enum TestConfiguration implements Configuration {
		HOST("test.testHost"),
		BROWSER("test.testBrowser");
		
		private String key;
		
		TestConfiguration(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
	}
	
	private SeleniumEnvironment seleniumEnvironment;
	private BromineEnvironment bromineEnvironment;
	private String host;
	private String browser;

	public DefaultTestEnvironment(ConfigurationValue[] configurations) {
		host = ConfigurationUtils.getConfigurationValue(configurations, TestConfiguration.HOST);
		browser = ConfigurationUtils.getConfigurationValue(configurations, TestConfiguration.BROWSER);
		this.seleniumEnvironment = new DefaultSeleniumEnvironment(configurations);
		this.bromineEnvironment = new DefaultBromineEnvironment(configurations);
	}

	public DefaultTestEnvironment(TestEnvironment testEnvironment) {
		if (testEnvironment != null) {
			this.host = testEnvironment.getHost();
			this.browser = testEnvironment.getBrowser();
		}
	}
	
	public DefaultTestEnvironment(SeleniumEnvironment seleniumEnvironment, BromineEnvironment bromineEnvironment, String host, Browsers browser) {
		this(seleniumEnvironment, bromineEnvironment, host, browser.toString());
	}
	
	public DefaultTestEnvironment(SeleniumEnvironment seleniumEnvironment, BromineEnvironment bromineEnvironment, String host, String browser) {
		this.seleniumEnvironment = seleniumEnvironment;
		this.bromineEnvironment = bromineEnvironment;
		this.host = host;
		this.browser = browser;
	}
	
	@Override
	public BromineEnvironment getBromineEnvironment() {
		return bromineEnvironment;
	}

	@Override
	public SeleniumEnvironment getSeleniumEnvironment() {
		return seleniumEnvironment;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public String getBrowser() {
		return browser;
	}

	public DefaultTestEnvironment merge(TestEnvironment testEnvironment) {
		if (testEnvironment == null) {
			return this;
		}
		if (host == null) {
			host = testEnvironment.getHost();
		}
		if (browser == null) {
			browser = testEnvironment.getBrowser();
		}
		if (seleniumEnvironment == null) {
			seleniumEnvironment = new DefaultSeleniumEnvironment(testEnvironment.getSeleniumEnvironment());
		} else {
			seleniumEnvironment = new DefaultSeleniumEnvironment(seleniumEnvironment);
			((DefaultSeleniumEnvironment)seleniumEnvironment).merge(testEnvironment.getSeleniumEnvironment());
		}
		
		if (bromineEnvironment == null) {
			bromineEnvironment = new DefaultBromineEnvironment(testEnvironment.getBromineEnvironment());
		} else {
			bromineEnvironment = new DefaultBromineEnvironment(bromineEnvironment);
			((DefaultBromineEnvironment)bromineEnvironment).merge(testEnvironment.getBromineEnvironment());
		}
		
		return this;
	}
}