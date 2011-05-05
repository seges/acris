package sk.seges.sesam.core.test.selenium.usecase;

import org.junit.Ignore;
import org.junit.Test;

import sk.seges.sesam.core.test.selenium.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.test.selenium.AbstractSeleniumTest;

@SeleniumTest
@Ignore
public class MockSelenise extends AbstractSeleniumTest {

	protected MockSelenise(TestEnvironment testEnvironment) {
		super(testEnvironment);
	}

	@Test
	public void testMethod1() {}

	@Test
	public void testMethod2() {}
}