package sk.seges.sesam.core.test.selenium.configuration;

import sk.seges.sesam.core.configuration.api.Configuration;
import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.configuration.utils.ConfigurationUtils;
import sk.seges.sesam.core.test.selenium.configuration.api.SeleniumEnvironment;

public class DefaultSeleniumEnvironment implements SeleniumEnvironment {

	public enum SeleniumConfiguration implements Configuration {
		HOST("test.seleniumHost"),
		PORT("test.seleniumPort");
		
		private String key;
		
		SeleniumConfiguration(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
	}
	
	private String host;
	private int port;
	
	public DefaultSeleniumEnvironment(ConfigurationValue[] configurations) {
		init(ConfigurationUtils.getConfigurationValue(configurations, SeleniumConfiguration.HOST),
			 ConfigurationUtils.getConfigurationInt(configurations, SeleniumConfiguration.PORT));
	}

	public DefaultSeleniumEnvironment(SeleniumEnvironment seleniumEnvironment) {
		if (seleniumEnvironment != null) {
			init(seleniumEnvironment.getSeleniumHost(),
				 seleniumEnvironment.getSeleniumPort());
		}
	}
	
	public DefaultSeleniumEnvironment(String host, int port) {
		init(host, port);
	}
	
	private void init(String host, int port) {
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
	
	public DefaultSeleniumEnvironment merge(SeleniumEnvironment seleniumEnvironment) {
		if (seleniumEnvironment == null) {
			return this;
		}
		
		if (host == null) {
			this.host = seleniumEnvironment.getSeleniumHost();
		}
		
		if (port == 0) {
			this.port = seleniumEnvironment.getSeleniumPort();
		}
		
		return this;
	}
}