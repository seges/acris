package sk.seges.sesam.pap.test.selenium.processor.model;

import sk.seges.sesam.pap.configuration.model.setting.SettingsTypeElement;


public class SeleniumSettingsContext {

	private SeleniumTestCaseType seleniumTestCaseType;
	
	private SettingsTypeElement settingsTypeElement;

	public SettingsTypeElement getSettings() {
		return settingsTypeElement;
	}

	public void setSettings(SettingsTypeElement settingsTypeElement) {
		this.settingsTypeElement = settingsTypeElement;
	}

	public SeleniumTestCaseType getSeleniumTestCase() {
		return seleniumTestCaseType;
	}

	public void setSeleniumTestCase(SeleniumTestCaseType seleniumTestCaseType) {
		this.seleniumTestCaseType = seleniumTestCaseType;
	}
}