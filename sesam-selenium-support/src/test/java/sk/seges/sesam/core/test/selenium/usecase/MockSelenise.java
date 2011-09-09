package sk.seges.sesam.core.test.selenium.usecase;

import org.junit.Ignore;
import org.junit.Test;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Credentials;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTestConfiguration;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.core.test.selenium.runner.MockRunner;

@Ignore
@SeleniumTest(suiteRunner = MockRunner.class)
@SeleniumTestConfiguration(
		testURL = "overridenURL"
)
@Credentials (
		username = "test",
		password = "pass"
)
public class MockSelenise extends AbstractSeleniumTest {

	protected MockSelenise(TestEnvironment testEnvironment) {
		super();
	}

	@Test
	public void testMethod1() {}

	@Test
	public void testMethod2() {}
}