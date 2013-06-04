package sk.seges.sesam.selenium;

import sk.seges.sesam.core.test.selenium.configuration.annotation.Selenium;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSuite;
import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;

@SeleniumSuite
@Selenium(testURL = "http://www.google.com",  testURI="/", browser = Browsers.FIREFOX)
public class SuiteRunner {}
