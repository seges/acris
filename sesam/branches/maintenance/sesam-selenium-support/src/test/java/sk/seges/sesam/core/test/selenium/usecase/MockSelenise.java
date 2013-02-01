package sk.seges.sesam.core.test.selenium.usecase;

import org.junit.Ignore;

import sk.seges.sesam.core.test.selenium.configuration.annotation.Credentials;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Selenium;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTestCase;
import sk.seges.sesam.core.test.selenium.runner.MockSuite;
import sk.seges.sesam.core.test.webdriver.AbstractWebdriverTest;
import sk.seges.sesam.core.test.webdriver.configuration.DefaultTestSettings;

@Ignore
@SeleniumTestCase(suiteRunner = MockSuite.class, configuration = TestConfiguration.class, description = "Test desc")
@Selenium(
		testURL = "overridenURL"
)
@Credentials (
		username = "test",
		password = "pass"
)
public class MockSelenise extends AbstractWebdriverTest {

	@SeleniumTest(description = "mock test method")
	public void testMethod1() {}

	@SeleniumTest(description = "mock test method 2")
	public void testMethod2() {}

	@Override
	protected DefaultTestSettings getSettings() {
		return null;
	}
}