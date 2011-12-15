package sk.seges.sesam.core.test.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import sk.seges.sesam.core.test.bromine.BromineTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;
import sk.seges.sesam.core.test.selenium.configuration.model.CoreSeleniumSettingsProvider;
import sk.seges.sesam.core.test.selenium.factory.WebDriverFactory;
import sk.seges.sesam.core.test.selenium.junit.runner.SeleniumTestRunner;
import sk.seges.sesam.core.test.selenium.report.LoggingWebDriverEventListener;
import sk.seges.sesam.core.test.selenium.report.ScreenshotsWebDriverEventListener;
import sk.seges.sesam.core.test.selenium.report.model.ReportEventListener;
import sk.seges.sesam.core.test.selenium.report.model.TestCaseResult;
import sk.seges.sesam.core.test.selenium.report.printer.HtmlTestReportPrinter;
import sk.seges.sesam.core.test.selenium.report.support.ScreenshotSupport;
import sk.seges.sesam.core.test.selenium.support.MailSupport;
import sk.seges.sesam.core.test.selenium.support.SeleniumSupport;
import sk.seges.sesam.core.test.webdriver.WebDriverActions;

@RunWith(SeleniumTestRunner.class)
public abstract class AbstractSeleniumTest extends BromineTest {

	protected MailSupport mailSupport;
	protected SeleniumSupport seleniumSupport;

	private String testName;
	protected Wait<WebDriver> wait;
	
	protected Actions actions;
	protected CoreSeleniumSettingsProvider settings;
	
	private ReportEventListener reportEventListener;
	
	protected AbstractSeleniumTest() {
		setTestEnvironment(ensureSettings().getSeleniumSettings());
	}

	protected abstract CoreSeleniumSettingsProvider getSettings();

	public CoreSeleniumSettingsProvider ensureSettings() {
		if (settings == null) {
			settings = getSettings();
		}
		
		return settings;
	}
	
	@Override
	protected WebDriver createWebDriver(WebDriverFactory webDriverFactory, SeleniumSettings seleniumSettings) {
		return new EventFiringWebDriver(super.createWebDriver(webDriverFactory, seleniumSettings));
	}
	
	public void setTestEnvironment(SeleniumSettings testEnvironment) {
		super.setTestEnvironment(testEnvironment);
		wait = new WebDriverWait(webDriver, 30);
		webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);

		actions = new WebDriverActions(webDriver, testEnvironment);
		
		mailSupport = getMailSupport();
		seleniumSupport = getSeleniumSupport();		
	}
	
	@Override
	protected String getTestName() {
		return testName != null ? testName : getClass().getSimpleName();
	}

	protected MailSupport getMailSupport() {
		return new MailSupport(ensureSettings().getMailSettings());
	}
	
	protected SeleniumSupport getSeleniumSupport() {
		return new SeleniumSupport(webDriver);
	}

	@Before
	public void setUp() {
		//this should maximize the browser window
		((JavascriptExecutor)webDriver).executeScript("if (window.screen){window.moveTo(0, 0);window.resizeTo(window.screen.availWidth,window.screen.availHeight);};", new Object[] {});
		webDriver.get(testEnvironment.getTestURL() + testEnvironment.getTestURI());

		ReportSettings reportSettings = ensureSettings().getReportSettings();
		
		reportEventListener = new ReportEventListener(this.getClass(), new HtmlTestReportPrinter(reportSettings));
		((EventFiringWebDriver)webDriver).register(reportEventListener);
		
		if (Boolean.TRUE.equals(reportSettings.getScreenshot().getSupport().getEnabled())) {
			reportEventListener.addTestResultCollector(new ScreenshotsWebDriverEventListener(new ScreenshotSupport(webDriver, reportSettings), reportSettings));
		}

		if (Boolean.TRUE.equals(reportSettings.getHtml().getSupport().getEnabled())) {
			reportEventListener.addTestResultCollector(new LoggingWebDriverEventListener());
		}
		
		reportEventListener.initialize();
	}

	@After
	public void tearDown() {

		reportEventListener.finish();

		super.tearDown();	
	}
	
	public TestCaseResult getTestInfo() {
		return reportEventListener.getTestInfo();
	}
}