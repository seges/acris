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
import sk.seges.sesam.core.test.selenium.model.EnvironmentInfo;
import sk.seges.sesam.core.test.selenium.report.LoggingWebDriverEventListener;
import sk.seges.sesam.core.test.selenium.report.ScreenshotsWebDriverEventListener;
import sk.seges.sesam.core.test.selenium.report.model.ReportEventListener;
import sk.seges.sesam.core.test.selenium.report.model.TestCaseResult;
import sk.seges.sesam.core.test.selenium.report.printer.HtmlTestReportPrinter;
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
	
	public void setUp(String testName) {
		this.testName = testName;
		setUp();
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
		return testName;
	}

	protected MailSupport getMailSupport() {
		return new MailSupport(ensureSettings().getMailSettings());
	}
	
	protected SeleniumSupport getSeleniumSupport() {
		return new SeleniumSupport(webDriver);
	}

	@Before
	public void setUp() {
		
		//this maximizes the browser window and returns the actual window width
		Object width = ((JavascriptExecutor)webDriver).executeScript("if (window.screen){window.moveTo(0, 0);window.resizeTo(window.screen.availWidth,window.screen.availHeight);}; if( typeof( window.innerWidth ) == 'number' ) { return window.innerWidth; } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) { return document.documentElement.clientWidth; } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) { return document.body.clientWidth; } return 0;", new Object[] {});
		
		EnvironmentInfo environmentInfo = new EnvironmentInfo();
		
		if (width != null) {
			environmentInfo.setWindowSize((Long)width);
		}

		webDriver.get(testEnvironment.getTestURL() + testEnvironment.getTestURI());

		if (testName != null) {
			ReportSettings reportSettings = ensureSettings().getReportSettings();
			
			reportEventListener = new ReportEventListener(this.getClass(), new HtmlTestReportPrinter(reportSettings), reportSettings, webDriver, environmentInfo, testName);
			((EventFiringWebDriver)webDriver).register(reportEventListener);
			registerAssertionListener(reportEventListener);
			
			if (Boolean.TRUE.equals(reportSettings.getScreenshot().getSupport().getEnabled())) {
				reportEventListener.addTestResultCollector(new ScreenshotsWebDriverEventListener(reportSettings, webDriver, environmentInfo));
			}
			
			if (Boolean.TRUE.equals(reportSettings.getHtml().getSupport().getEnabled())) {
				reportEventListener.addTestResultCollector(new LoggingWebDriverEventListener(reportSettings, webDriver, environmentInfo));
			}
	
			reportEventListener.initialize();
			
		}
		navigateToTestPage();
	}

	@After
	public void tearDown() {

		try {
			if (reportEventListener != null) {
				reportEventListener.finish();
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		
		super.tearDown();	
	}
	
	public TestCaseResult getTestInfo() {
		return reportEventListener.getTestInfo();
	}
	
	protected void navigateToTestPage () {
		webDriver.get(testEnvironment.getTestURL() + testEnvironment.getTestURI());
		
		
	}
}