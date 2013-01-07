package sk.seges.sesam.core.test.webdriver;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;
import sk.seges.sesam.core.test.selenium.configuration.model.CoreSeleniumSettingsProvider;
import sk.seges.sesam.core.test.selenium.junit.runner.SeleniumTestRunner;
import sk.seges.sesam.core.test.webdriver.factory.WebDriverFactory;
import sk.seges.sesam.core.test.webdriver.model.EnvironmentInfo;
import sk.seges.sesam.core.test.webdriver.report.LoggingWebDriverEventListener;
import sk.seges.sesam.core.test.webdriver.report.ScreenshotsWebDriverEventListener;
import sk.seges.sesam.core.test.webdriver.report.model.ReportEventListener;
import sk.seges.sesam.core.test.webdriver.report.model.TestCaseResult;
import sk.seges.sesam.core.test.webdriver.report.printer.HtmlTestReportPrinter;
import sk.seges.sesam.core.test.webdriver.support.MailSupport;
import sk.seges.sesam.core.test.webdriver.support.SeleniumSupport;

@RunWith(SeleniumTestRunner.class)
public abstract class AbstractWebdriverTest extends AbstractAssertions {

	protected MailSupport mailSupport;
	protected SeleniumSupport seleniumSupport;

	private String testName;
	protected Wait<WebDriver> wait;

	protected ReportSettings reportSettings;

	protected Actions actions;
	protected CoreSeleniumSettingsProvider settings;
	
	private ReportEventListener reportEventListener;
	private EnvironmentInfo environmentInfo;
	
	protected AbstractWebdriverTest() {
		setTestEnvironment(ensureSettings().getSeleniumSettings());
		actions = new WebDriverActions(webDriver, testEnvironment);
		environmentInfo = new EnvironmentInfo();
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
	
	public final void setUp(String testName) {
		this.testName = testName;

		if (testName != null) {
			reportSettings = ensureSettings().getReportSettings();
			
			this.testName = testName;
			reportEventListener = new ReportEventListener(this.getClass(), new HtmlTestReportPrinter(reportSettings), reportSettings, webDriver, environmentInfo);
			actions = new LoggingActions(webDriver, reportEventListener, testEnvironment);	
		}
		
		setUp();
	}
	
	public void setTestEnvironment(SeleniumSettings testEnvironment) {
		super.setTestEnvironment(testEnvironment);
		
		wait = new WebDriverWait(webDriver, 30);
		webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);

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
		
		if (width != null) {
			environmentInfo.setWindowSize((Long)width);
		}

		final String url = testEnvironment.getTestURL() + testEnvironment.getTestURI();
		webDriver.get(url);

		wait.until(new Function<WebDriver, Boolean>() {
			
			@Override
			public Boolean apply(WebDriver arg0) {
				return webDriver.getCurrentUrl().startsWith(url);
			}
		});

		//wait to any HTML
		webDriver.findElement(By.xpath("//*"));
		
		//if running as regular junit
		if (testName != null) {
			
			this.reportEventListener.setTestMethod(testName);
			
			registerAssertionListener(reportEventListener);

			((EventFiringWebDriver)webDriver).register(reportEventListener);

			if (Boolean.TRUE.equals(reportSettings.getHtml().getSupport().getEnabled())) {
				reportEventListener.addTestResultCollector(new LoggingWebDriverEventListener(reportSettings, webDriver, environmentInfo));
			}

			if (Boolean.TRUE.equals(reportSettings.getScreenshot().getSupport().getEnabled())) {
				reportEventListener.addTestResultCollector(new ScreenshotsWebDriverEventListener(reportSettings, webDriver, environmentInfo));
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
				((EventFiringWebDriver)webDriver).unregister(reportEventListener);
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
	
	public ReportEventListener getReportEventListener() {
		return reportEventListener;
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}
}