package sk.seges.sesam.core.test.selenium.configuration;

import sk.seges.sesam.core.test.selenium.configuration.api.BromineEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;
import sk.seges.sesam.core.test.selenium.configuration.api.SeleniumEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;

public class DefaultTestEnvironment implements TestEnvironment {

	private SeleniumEnvironment seleniumEnvironment;
	private BromineEnvironment bromineEnvironment;
	private String host;
	private String browser;

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
	public String getTestHost() {
		return host;
	}

	@Override
	public String getBrowser() {
		return browser;
	}
}