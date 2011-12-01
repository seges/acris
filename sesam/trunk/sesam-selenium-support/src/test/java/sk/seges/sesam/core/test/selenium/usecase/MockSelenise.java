package sk.seges.sesam.core.test.selenium.usecase;

import org.junit.Ignore;
import org.junit.Test;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.DefaultTestSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Credentials;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Selenium;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTestCase;
import sk.seges.sesam.core.test.selenium.runner.MockSuite;

@Ignore
@SeleniumTestCase(suiteRunner = MockSuite.class)
@Selenium(
		testURL = "overridenURL"
)
@Credentials (
		username = "test",
		password = "pass"
)
public class MockSelenise extends AbstractSeleniumTest {

	@Test
	public void testMethod1() {}

	@Test
	public void testMethod2() {}

	@Override
	protected DefaultTestSettings getSettings() {
		return null;
	}
}