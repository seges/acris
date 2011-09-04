package sk.seges.sesam.core.test.selenium.configuration;

import sk.seges.sesam.core.configuration.api.Configuration;
import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.configuration.utils.ConfigurationUtils;
import sk.seges.sesam.core.test.selenium.configuration.api.BromineEnvironment;

public class DefaultBromineEnvironment implements BromineEnvironment {

	public enum BromineConfiguration implements Configuration {
		HOST("test.bromineHost"),
		PORT("test.brominePort"),
		ENABLED("test.bromineEnabled"),
		;
		
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
	private Boolean enabled;

	public DefaultBromineEnvironment(BromineEnvironment bromineEnvironment) {
		if (bromineEnvironment != null) {
			init(bromineEnvironment.getBromineHost(),
				 bromineEnvironment.getBrominePort(),
				 bromineEnvironment.isBromineEnabled());
		}
	}

	public DefaultBromineEnvironment(ConfigurationValue[] configurations) {
		init(ConfigurationUtils.getConfigurationValue(configurations, BromineConfiguration.HOST),
			 ConfigurationUtils.getConfigurationInt(configurations, BromineConfiguration.PORT),
			 ConfigurationUtils.getConfigurationBoolean(configurations, BromineConfiguration.ENABLED));
	}
	
	public DefaultBromineEnvironment(String host, int port, Boolean enabled) {
		init(host, port, enabled);
	}
	
	protected void init(String host, int port, Boolean enabled) {
		this.host = host;
		this.port = port;
		this.enabled = enabled;
	}
	
	@Override
	public String getBromineHost() {
		return host;
	}

	@Override
	public int getBrominePort() {
		return port;
	}

	@Override
	public Boolean isBromineEnabled() {
		return enabled;
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

		if (enabled == null) {
			this.enabled = bromineEnvironment.isBromineEnabled();
		}
		
		return this;
	}
}