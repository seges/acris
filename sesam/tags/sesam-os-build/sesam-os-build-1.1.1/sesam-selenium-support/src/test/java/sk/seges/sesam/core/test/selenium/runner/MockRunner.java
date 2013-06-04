package sk.seges.sesam.core.test.selenium.runner;

import sk.seges.sesam.core.test.selenium.annotation.MailConfiguration;
import sk.seges.sesam.core.test.selenium.annotation.MailConfiguration.Provider;
import sk.seges.sesam.core.test.selenium.annotation.ReportConfiguration.ScreenshotConfiguration;
import sk.seges.sesam.core.test.selenium.annotation.ReportConfiguration;
import sk.seges.sesam.core.test.selenium.annotation.SeleniumTestConfiguration;
import sk.seges.sesam.core.test.selenium.annotation.SeleniumSuite;

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
