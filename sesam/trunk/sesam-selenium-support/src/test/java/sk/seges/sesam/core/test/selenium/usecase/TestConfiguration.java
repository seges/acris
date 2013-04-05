package sk.seges.sesam.core.test.selenium.usecase;

import sk.seges.sesam.core.test.selenium.configuration.annotation.Mail;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Mail.Provider;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Report;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Report.HtmlReport;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Report.Screenshot;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Report.Support;
import sk.seges.sesam.core.test.selenium.configuration.annotation.Selenium;

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
		html = @HtmlReport(support = @Support(enabled = true, directory = "result"), testTemplatePath = "path to the report"),
		screenshot = @Screenshot(support = @Support(enabled = true, directory = "screenshots"))
)
public interface TestConfiguration {}
