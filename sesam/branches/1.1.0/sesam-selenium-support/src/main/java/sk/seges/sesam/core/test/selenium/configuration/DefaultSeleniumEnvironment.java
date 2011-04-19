package sk.seges.sesam.core.test.selenium.configuration;

import sk.seges.sesam.core.test.selenium.configuration.api.SeleniumEnvironment;

public class DefaultSeleniumEnvironment implements SeleniumEnvironment {

	private String host;
	private int port;
	
	public DefaultSeleniumEnvironment(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public String getSeleniumHost() {
		return host;
	}

	@Override
	public int getSeleniumPort() {
		return port;
	}
}