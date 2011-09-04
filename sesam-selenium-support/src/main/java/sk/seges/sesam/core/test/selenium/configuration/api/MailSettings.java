package sk.seges.sesam.core.test.selenium.configuration.api;

import sk.seges.sesam.core.test.selenium.configuration.annotation.MailConfiguration.Provider;

public interface MailSettings {

	String getHost();

	String getPassword();
	
	String getMail();
	
	Provider getProvider();
}