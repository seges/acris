package sk.seges.sesam.core.test.selenium.configuration;

import sk.seges.sesam.core.test.selenium.configuration.api.BromineEnvironment;

public class DefaultBromineEnvironment implements BromineEnvironment {

	private String host;
	private int port;
	
	public DefaultBromineEnvironment(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public String getBromineHost() {
		return host;
	}

	@Override
	public int getBrominePort() {
		return port;
	}
}