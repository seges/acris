package sk.seges.sesam.core.test.selenium.configuration;

import sk.seges.sesam.core.configuration.api.Configuration;
import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.configuration.utils.ConfigurationUtils;
import sk.seges.sesam.core.test.selenium.configuration.api.BromineEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;
import sk.seges.sesam.core.test.selenium.configuration.api.SeleniumEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;

public class DefaultTestEnvironment implements TestEnvironment {

	public enum TestConfiguration implements Configuration {
		HOST("test.testHost"),
		URI("test.testUri"),
		REMOTE("test.testRemote"),
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
	private String uri;
	private String browser;
	private Boolean remote;

	public DefaultTestEnvironment(ConfigurationValue[] configurations) {
		host = ConfigurationUtils.getConfigurationValue(configurations, TestConfiguration.HOST);
		uri = ConfigurationUtils.getConfigurationValue(configurations, TestConfiguration.URI);
		browser = ConfigurationUtils.getConfigurationValue(configurations, TestConfiguration.BROWSER);
		remote = ConfigurationUtils.getConfigurationBoolean(configurations, TestConfiguration.REMOTE);
		this.seleniumEnvironment = new DefaultSeleniumEnvironment(configurations);
		this.bromineEnvironment = new DefaultBromineEnvironment(configurations);
	}

	public DefaultTestEnvironment(TestEnvironment testEnvironment) {
		if (testEnvironment != null) {
			this.host = testEnvironment.getHost();
			this.uri = testEnvironment.getUri();
			this.browser = testEnvironment.getBrowser();
			this.remote = testEnvironment.isRemote();
		}
	}
	
	public DefaultTestEnvironment(SeleniumEnvironment seleniumEnvironment, BromineEnvironment bromineEnvironment, String host, String uri, Browsers browser, Boolean remote) {
		this(seleniumEnvironment, bromineEnvironment, host, uri, browser.toString(), remote);
	}
	
	public DefaultTestEnvironment(SeleniumEnvironment seleniumEnvironment, BromineEnvironment bromineEnvironment, String host, String uri, String browser, Boolean remote) {
		this.seleniumEnvironment = seleniumEnvironment;
		this.bromineEnvironment = bromineEnvironment;
		this.host = host;
		this.browser = browser;
		this.uri = uri;
		this.remote = remote;
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

	@Override
	public String getUri() {
		return uri;
	}
	
	public DefaultTestEnvironment merge(TestEnvironment testEnvironment) {
		if (testEnvironment == null) {
			return this;
		}
		if (host == null) {
			host = testEnvironment.getHost();
		}
		
		if (uri == null) {
			uri = testEnvironment.getUri();
		}

		if (browser == null) {
			browser = testEnvironment.getBrowser();
		}

		if (remote == null) {
			remote = testEnvironment.isRemote();
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

	@Override
	public Boolean isRemote() {
		return remote;
	}
	
	public void setRemote(Boolean remote) {
		this.remote = remote;
	}
}