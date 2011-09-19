package sk.seges.sesam.core.test.selenium.report.support;

import org.openqa.selenium.support.events.EventFiringWebDriver;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.report.AbstractReportHelper;
import sk.seges.sesam.core.test.selenium.report.LoggingWebDriverEventListener;
import sk.seges.sesam.core.test.selenium.report.printer.HtmlReportPrinter;

public class HtmlReportSupport extends AbstractReportHelper implements ReportSupport {

	private LoggingWebDriverEventListener loggingWebDriverEventListener;

	public HtmlReportSupport(Class<? extends AbstractSeleniumTest> testClass, ReportSettings reportSettings) {
		loggingWebDriverEventListener = new LoggingWebDriverEventListener(testClass, new HtmlReportPrinter(reportSettings));
	}

	@Override
	public void initialize() {
		loggingWebDriverEventListener.initialize();
	}
	
	public EventFiringWebDriver registerTo(EventFiringWebDriver eventFiringWebDriver) {
		eventFiringWebDriver.register(loggingWebDriverEventListener);
		return eventFiringWebDriver;
	}

	@Override
	public void finish() {
	}
}
