package sk.seges.sesam.core.test.selenium.usecase;

import org.junit.Ignore;
import org.junit.Test;

import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTestCase;
import sk.seges.sesam.core.test.selenium.configuration.model.CoreSeleniumSettingsProvider;
import sk.seges.sesam.core.test.selenium.runner.MockSuite;
import sk.seges.sesam.core.test.webdriver.AbstractWebdriverTest;

@Ignore
@SeleniumTestCase(suiteRunner = MockSuite.class, configuration = TestConfiguration.class, description = "Test desc")
public class AdvancedMockSelenise extends AbstractWebdriverTest {

	public AdvancedMockSelenise() {
		super();
	}
	
	public AdvancedMockSelenise(String param) { this(); };

	protected AdvancedMockSelenise(int port) { this(); };

	AdvancedMockSelenise(boolean flags) { this(); };

	@SuppressWarnings("unused")
	private AdvancedMockSelenise(long port) { this(); };

	@Test
	public void testMethod1() {}

	@Test
	public void testMethod2() {}

	@Override
	protected CoreSeleniumSettingsProvider getSettings() {
		return null;
	}
}