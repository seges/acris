package sk.seges.sesam.core.test.selenium.runner;

import sk.seges.sesam.core.test.selenium.configuration.annotation.Mail;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Report;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Selenium;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSuite;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Mail.Provider;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Report.Support;

@SeleniumSuite
@Selenium(
		testURL="testURL"
)
@Mail(
		host = "host",
		mail = "mail",
		password = "pass",
		provider = Provider.IMAPS
)
@Report(
		html = @Support(enabled = true, directory = "result"),
		screenshot = @Support(enabled = true, directory = "screenshots")
)
public class MockSuite {

}
