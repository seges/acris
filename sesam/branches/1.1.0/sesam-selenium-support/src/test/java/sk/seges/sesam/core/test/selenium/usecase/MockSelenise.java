package sk.seges.sesam.core.test.selenium.usecase;

import sk.seges.sesam.core.test.selenium.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.annotation.TestCase;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.test.selenium.AbstractSeleniumTest;

@SeleniumTest
public class MockSelenise extends AbstractSeleniumTest {

	protected MockSelenise(TestEnvironment testEnvironment) {
		super(testEnvironment);
	}

	@TestCase
	public void testMethod1() {}

	@TestCase
	public void testMethod2() {}
}