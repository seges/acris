package sk.seges.sesam.core.test.selenium.configuration;

import sk.seges.sesam.core.test.selenium.configuration.api.BromineEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.Configuration;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.ConfigurationValue;
import sk.seges.sesam.core.test.selenium.configuration.utils.ConfigurationUtils;

public class DefaultBromineEnvironment implements BromineEnvironment {

	public enum BromineConfiguration implements Configuration {
		HOST("test.bromineHost"),
		PORT("test.brominePort");
		
		private String key;
		
		BromineConfiguration(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
	}

	private String host;
	private int port;

	public DefaultBromineEnvironment(BromineEnvironment bromineEnvironment) {
		if (bromineEnvironment != null) {
			init(bromineEnvironment.getBromineHost(),
				 bromineEnvironment.getBrominePort());
		}
	}

	public DefaultBromineEnvironment(ConfigurationValue[] configurations) {
		init(ConfigurationUtils.getConfigurationValue(configurations, BromineConfiguration.HOST),
			 ConfigurationUtils.getConfigurationNumber(configurations, BromineConfiguration.PORT));
	}
	
	public DefaultBromineEnvironment(String host, int port) {
		init(host, port);
	}
	
	protected void init(String host, int port) {
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

	public DefaultBromineEnvironment merge(BromineEnvironment bromineEnvironment) {
		if (bromineEnvironment == null) {
			return this;
		}
		
		if (host == null) {
			this.host = bromineEnvironment.getBromineHost();
		}
		
		if (port == 0) {
			this.port = bromineEnvironment.getBrominePort();
		}
		
		return this;
	}
}