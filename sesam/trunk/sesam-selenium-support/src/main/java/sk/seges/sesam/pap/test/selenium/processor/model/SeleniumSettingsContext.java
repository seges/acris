package sk.seges.sesam.pap.test.selenium.processor.model;

import sk.seges.sesam.pap.configuration.model.setting.SettingsTypeElement;


public class SeleniumSettingsContext {

	private SeleniumTestTypeElement seleniumTestTypeElement;
	
	private SettingsTypeElement settingsTypeElement;

	public SettingsTypeElement getSettings() {
		return settingsTypeElement;
	}

	public void setSettings(SettingsTypeElement settingsTypeElement) {
		this.settingsTypeElement = settingsTypeElement;
	}

	public SeleniumTestTypeElement getSeleniumTest() {
		return seleniumTestTypeElement;
	}

	public void setSeleniumTest(SeleniumTestTypeElement seleniumTestTypeElement) {
		this.seleniumTestTypeElement = seleniumTestTypeElement;
	}
}