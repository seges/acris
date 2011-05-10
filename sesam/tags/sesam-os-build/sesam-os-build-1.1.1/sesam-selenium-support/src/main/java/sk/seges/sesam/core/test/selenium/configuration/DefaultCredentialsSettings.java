package sk.seges.sesam.core.test.selenium.configuration;

import sk.seges.sesam.core.test.selenium.configuration.api.CredentialsSettings;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.Configuration;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.ConfigurationValue;
import sk.seges.sesam.core.test.selenium.configuration.utils.ConfigurationUtils;

public class DefaultCredentialsSettings implements CredentialsSettings {

	public enum CredentialsConfiguration implements Configuration {
		USERNAME("test.username"),
		PASSWORD("test.password");
		
		private String key;
		
		CredentialsConfiguration(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
	}

	private String username;
	private String password;
	
	public DefaultCredentialsSettings(CredentialsSettings credentialsSettings) {
		if (credentialsSettings != null) {
			init(credentialsSettings.getUsername(), credentialsSettings.getPassword());
		}
	}

	public DefaultCredentialsSettings(ConfigurationValue[] configurations) {
		init(ConfigurationUtils.getConfigurationValue(configurations, CredentialsConfiguration.USERNAME),
			 ConfigurationUtils.getConfigurationValue(configurations, CredentialsConfiguration.PASSWORD));
	}
	
	public DefaultCredentialsSettings(String username, String password) {
		init(username, password);
	}
	
	protected void init(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public DefaultCredentialsSettings merge(CredentialsSettings credentialsSettings) {
		if (credentialsSettings == null) {
			return this;
		}
		
		if (username == null) {
			this.username = credentialsSettings.getUsername();
		}
		
		if (password == null) {
			this.password = credentialsSettings.getPassword();
		}
		
		return this;
	}
}