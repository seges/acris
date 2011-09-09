package sk.seges.sesam.core.test.selenium.runner;

import sk.seges.sesam.core.test.selenium.configuration.annotation.MailConfiguration;
import sk.seges.sesam.core.test.selenium.configuration.annotation.MailConfiguration.Provider;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportConfiguration;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportConfiguration.ScreenshotConfiguration;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSuite;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTestConfiguration;

@SeleniumSuite
@SeleniumTestConfiguration(
		testURL="testURL"
)
@MailConfiguration(
		host = "host",
		mail = "mail",
		password = "pass",
		provider = Provider.IMAPS
)
@ReportConfiguration(
		screenshotConfiguration = @ScreenshotConfiguration(
				produceScreenshots = true,
				resultDirectory = "result",
				screenshotsDirectory = "screenshots"
		)
)
public class MockRunner {

}
