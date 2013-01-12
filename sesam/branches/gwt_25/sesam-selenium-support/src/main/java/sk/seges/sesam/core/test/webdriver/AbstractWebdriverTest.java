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

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;
import sk.seges.sesam.core.test.selenium.configuration.model.CoreSeleniumSettingsProvider;
import sk.seges.sesam.core.test.selenium.junit.runner.SeleniumTestRunner;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperation;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationState;
import sk.seges.sesam.core.test.webdriver.api.Assertion;
import sk.seges.sesam.core.test.webdriver.api.SesamWebDriver;
import sk.seges.sesam.core.test.webdriver.factory.LocalWebDriverFactory;
import sk.seges.sesam.core.test.webdriver.factory.RemoteWebDriverFactory;
import sk.seges.sesam.core.test.webdriver.factory.WebDriverFactory;
import sk.seges.sesam.core.test.webdriver.model.EnvironmentInfo;
import sk.seges.sesam.core.test.webdriver.model.SesamWebDriverDelegate;
import sk.seges.sesam.core.test.webdriver.report.LoggingAssertionDelegate;
import sk.seges.sesam.core.test.webdriver.report.LoggingWebDriverEventListener;
import sk.seges.sesam.core.test.webdriver.report.ScreenshotsWebDriverEventListener;
import sk.seges.sesam.core.test.webdriver.report.model.CommandResult;
import sk.seges.sesam.core.test.webdriver.report.model.ReportEventListener;
import sk.seges.sesam.core.test.webdriver.report.model.TestCaseResult;
import sk.seges.sesam.core.test.webdriver.report.printer.HtmlTestReportPrinter;
import sk.seges.sesam.core.test.webdriver.report.support.ScreenshotSupport;
import sk.seges.sesam.core.test.webdriver.support.MailSupport;
import sk.seges.sesam.core.test.webdriver.support.SeleniumSupport;

import com.google.common.base.Function;

@RunWith(SeleniumTestRunner.class)
public abstract class AbstractWebdriverTest {

	public class ReportContext {

		private CommandResult commandResult;
		private String screenshotName;
		
		public CommandResult setCommandResult(CommandResult commandResult) {
			this.commandResult = commandResult;
			return commandResult;
		}
		
		public void setScreenshotName(String screenshotName) {
			this.screenshotName = screenshotName;
		}
		
		public CommandResult getCommandResult() {
			return commandResult;
		}
		
		public String getScreenshotName() {
			return screenshotName;
		}
	}
	
	protected MailSupport mailSupport;
	protected SeleniumSupport seleniumSupport;
	protected Assertion assertionSupport;

	protected SesamWebDriver webDriver;

	private String testName;
	protected Wait<WebDriver> wait;

	protected ReportSettings reportSettings;
	private ReportEventListener reportEventListener;
	private boolean reportEventListenerInitialized = false;
	
	protected Actions actions;
	protected CoreSeleniumSettingsProvider settings;
	
	private EnvironmentInfo environmentInfo;

	protected SeleniumSettings testEnvironment;
	private ScreenshotSupport screenshotSupport;
	private ReportContext reportContext;
	
	protected AbstractWebdriverTest() {
		setTestEnvironment(ensureSettings().getSeleniumSettings());
		this.actions = new WebDriverActions(webDriver, testEnvironment);
		this.environmentInfo = new EnvironmentInfo();
	}

	protected abstract CoreSeleniumSettingsProvider getSettings();

	protected Assertion getAssertion() {
		if (assertionSupport != null) {
			return assertionSupport;
		}

		assertionSupport = new JunitAssertionWrapper();
		if (reportSettings != null && reportSettings.getHtml() != null && Boolean.TRUE.equals(reportSettings.getHtml().getSupport().getEnabled())) {
			assertionSupport = new LoggingAssertionDelegate((JunitAssertionWrapper)assertionSupport);
		}
		
		return assertionSupport;
	}
	
	protected WebDriverFactory getWebDriverFactory(SeleniumSettings testEnvironment) {
		return testEnvironment.getRemoteServerURL() == null ? 
				new LocalWebDriverFactory() : new RemoteWebDriverFactory();
	}

	public void runTests() {};

	public CoreSeleniumSettingsProvider ensureSettings() {
		if (settings == null) {
			settings = getSettings();
		}
		
		return settings;
	}
	
	protected WebDriver createWebDriver(WebDriverFactory webDriverFactory, SeleniumSettings seleniumSettings) {
		return new EventFiringWebDriver(webDriverFactory.createSelenium(seleniumSettings));
	}
	
	public final void setUp(String testName) {
		this.testName = testName;

		if (testName != null) {
			reportSettings = ensureSettings().getReportSettings();
			
			this.testName = testName;
			
			actions = new LoggingActions(webDriver, getReportEventListener(), testEnvironment);	
		}

		setUp();
	}
	
	public void setTestEnvironment(SeleniumSettings testEnvironment) {
		this.testEnvironment = testEnvironment;
		this.webDriver = new SesamWebDriverDelegate(createWebDriver(getWebDriverFactory(testEnvironment), testEnvironment));
		
		this.wait = new WebDriverWait(webDriver, 30);
		this.webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
	}
	
	protected String getTestName() {
		return testName;
	}

	protected MailSupport getMailSupport() {
		return new MailSupport(ensureSettings().getMailSettings());
	}
	
	public ReportEventListener getReportEventListener() {
		if (reportEventListenerInitialized) {
			return reportEventListener;
		}
		reportEventListenerInitialized = true;

		if (testName != null) {
			reportEventListener = 
				new ReportEventListener(this.getClass(), new HtmlTestReportPrinter(reportSettings), reportSettings, webDriver, environmentInfo);
		}
		
		return reportEventListener;
	}
	
	protected SeleniumSupport getSeleniumSupport() {
		return new SeleniumSupport(getReportEventListener(), webDriver, wait);
	}

	@Before
	public void setUp() {
		
		this.mailSupport = getMailSupport();
		this.seleniumSupport = getSeleniumSupport();

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
			
			getReportEventListener().setTestMethod(testName);
			
			((EventFiringWebDriver)webDriver).register(getReportEventListener());

			if (Boolean.TRUE.equals(reportSettings.getHtml().getSupport().getEnabled())) {
				getReportEventListener().addTestResultCollector(new LoggingWebDriverEventListener(reportSettings, webDriver, environmentInfo));
				((LoggingAssertionDelegate)getAssertion()).registerAssertionListener(getReportEventListener());
			}

			if (Boolean.TRUE.equals(reportSettings.getScreenshot().getSupport().getEnabled())) {

				reportContext = new ReportContext();
				screenshotSupport = new ScreenshotSupport(reportContext, webDriver, reportSettings, environmentInfo);
				
				getReportEventListener().addTestResultCollector(new ScreenshotsWebDriverEventListener(reportSettings, screenshotSupport, reportContext, webDriver, environmentInfo));
			}
				
			getReportEventListener().initialize();
		}
		
		navigateToTestPage();
	}

	@After
	public void tearDown() {

		try {
			if (getReportEventListener() != null) {
				getReportEventListener().finish();
				((EventFiringWebDriver)webDriver).unregister(getReportEventListener());
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		
		webDriver.quit();
	}
	
	public TestCaseResult getTestInfo() {
		return getReportEventListener().getTestInfo();
	}
	
	protected void navigateToTestPage () {

		webDriver.get(testEnvironment.getTestURL() + testEnvironment.getTestURI());		

		if (getReportEventListener() != null) {
			getReportEventListener().collectResults(false);
		}
		
		((JavascriptExecutor)webDriver).executeScript("document.ajax_outstanding = 0;", new Object[] {});

		String ajaxRequestCounterScript =  
			"XMLHttpRequest.prototype.uniqueID = function() {" + 
			"	if (!this.uniqueIDMemo) {" + 
			"		this.uniqueIDMemo = Math.floor(Math.random() * 1000);" + 
			"	}" + 
			"	return this.uniqueIDMemo;" + 
			"};" + 
			"XMLHttpRequest.prototype.oldOpen = XMLHttpRequest.prototype.open;" + 
			"var newOpen = function(method, url, async, user, password) {" + 
			"	ajaxRequestStarted = 'open';" + 
			"	document.ajax_outstanding++;" + 
			"	console.log('started ' + document.ajax_outstanding);" +
			"	this.oldOpen(method, url, async, user, password);" + 
			"};" + 
			"XMLHttpRequest.prototype.open = newOpen;" + 
			"XMLHttpRequest.prototype.oldSend = XMLHttpRequest.prototype.send;" + 
			"var newSend = function(a) {" + 
			"	var xhr = this;" + 
			"	var onload = function() {" + 
			"		ajaxRequestComplete = 'loaded';" + 
			"		document.ajax_outstanding--;" + 
			"		console.log('finished ' + document.ajax_outstanding);" +
			"	};" + 
			"	var onerror = function() {" + 
			"		ajaxRequestComplete = 'Err';" + 
			"		document.ajax_outstanding--;" + 
			"		console.log('finished ' + document.ajax_outstanding);" +
			"	};" + 
			"	var onabort = function() {" + 
			"		ajaxRequestComplete = 'Abort';" + 
			"		document.ajax_outstanding--;" + 
			"		console.log('aborted ' + document.ajax_outstanding);" +
			"	};" + 
			"	xhr.addEventListener('load', onload, false);" + 
			"	xhr.addEventListener('error', onerror, false);" + 
			"	xhr.addEventListener('abbort', onabort, false);" + 
			"	xhr.oldSend(a);" + 
			"};" +
			"XMLHttpRequest.prototype.send = newSend;";
		
		((JavascriptExecutor)webDriver).executeScript(ajaxRequestCounterScript, new Object[] {});

		if (getReportEventListener() != null) {
			getReportEventListener().collectResults(true);
		}

		seleniumSupport.waitUntilLoaded();
		
		if (reportContext != null && reportContext.getCommandResult() != null && 
				reportContext.getCommandResult().getOperation().equals(SeleniumOperation.NAVIGATE_TO) &&
				reportContext.getCommandResult().getState().equals(SeleniumOperationState.AFTER)) {
			screenshotSupport.makeScreenshot(reportContext.getScreenshotName());
		}
	}
	
	public WebDriver getWebDriver() {
		return webDriver;
	}
}