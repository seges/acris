package sk.seges.sesam.core.test.selenium.configuration;

import sk.seges.sesam.core.configuration.api.Configuration;
import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.configuration.utils.ConfigurationUtils;
import sk.seges.sesam.core.test.selenium.configuration.annotation.MailConfiguration.Provider;
import sk.seges.sesam.core.test.selenium.configuration.api.MailSettings;

public class DefaultMailSettings implements MailSettings {

	public enum MailSettingsConfiguration implements Configuration {

		HOST("mail.host"),
		PASSWORD("mail.password"),
		MAIL("mail.address"),
		PROVIDER("mail.provider");

		private String key;
		
		MailSettingsConfiguration(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
	}

	private String host;
	private String password;
	private String address;
	private Provider provider;
	
	public DefaultMailSettings(ConfigurationValue[] configurations) {
		init(ConfigurationUtils.getConfigurationValue(configurations, MailSettingsConfiguration.HOST),
			 ConfigurationUtils.getConfigurationValue(configurations, MailSettingsConfiguration.PASSWORD),
			 ConfigurationUtils.getConfigurationValue(configurations, MailSettingsConfiguration.MAIL),
			 ConfigurationUtils.getConfigurationValue(configurations, MailSettingsConfiguration.PROVIDER));
	}

	public DefaultMailSettings(MailSettings mailEnvironment) {
		if (mailEnvironment != null) {
			init(mailEnvironment.getHost(), mailEnvironment.getPassword(),
				 mailEnvironment.getMail(), mailEnvironment.getProvider());
		}
	}

	public DefaultMailSettings(String host, String password, String address, Provider provider) {
		init(host, password, address, provider);
	}

	public DefaultMailSettings(String host, String password, String address, String provider) {
		init(host, password, address, provider);
	}
	
	private void init(String host, String password, String address, String provider) {
		Provider result = null;
		for (Provider p: Provider.values()) {
			if (p.toString().equals(provider)) {
				result = p;
			}
		}
		init(host, password, address, result);
	}

	private void init(String host, String password, String address, Provider provider) {
		this.host = host;
		this.password = password;
		this.address =  address;
		this.provider = provider;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getMail() {
		return address;
	}

	@Override
	public Provider getProvider() {
		return provider;
	}

	public DefaultMailSettings merge(MailSettings mailEnvironment) {
		if (mailEnvironment == null) {
			return this;
		}
		
		if (host == null) {
			this.host = mailEnvironment.getHost();
		}
		
		if (password == null) {
			this.password = mailEnvironment.getPassword();
		}

		if (provider == null) {
			this.provider = mailEnvironment.getProvider();
		}

		if (address == null) {
			this.address = mailEnvironment.getMail();
		}

		return this;
	}
}