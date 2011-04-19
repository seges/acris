package sk.seges.sesam.core.test.selenium.usecase;

import sk.seges.sesam.core.test.selenium.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.annotation.TestCase;

@SeleniumTest
public class AdvancedMockSelenise {

	public AdvancedMockSelenise() {}
	
	public AdvancedMockSelenise(String param) {};

	protected AdvancedMockSelenise(int port) {};

	AdvancedMockSelenise(boolean flags) {};

	@SuppressWarnings("unused")
	private AdvancedMockSelenise(long port) {};

	@TestCase
	public void testMethod1() {}

	@TestCase
	public void testMethod2() {}
}