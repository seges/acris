package sk.seges.sesam.core.test.selenium.configuration.api;


public interface TestEnvironment {

	BromineEnvironment getBromineEnvironment();
	
	SeleniumEnvironment getSeleniumEnvironment();

	String getTestHost();

	String getBrowser();
}
