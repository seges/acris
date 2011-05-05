package sk.seges.sesam.core.test.selenium.usecase;

import org.junit.Test;

import sk.seges.sesam.core.test.selenium.annotation.SeleniumTest;
import sk.seges.sesam.test.selenium.AbstractSeleniumTest;

@SeleniumTest
public class AdvancedMockSelenise extends AbstractSeleniumTest {

	public AdvancedMockSelenise() {
		super(null);
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
}