package sk.seges.sesam.core.test.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import sk.seges.sesam.core.test.bromine.BromineTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;
import sk.seges.sesam.core.test.selenium.configuration.model.CoreSeleniumSettingsProvider;
import sk.seges.sesam.core.test.selenium.factory.WebDriverFactory;
import sk.seges.sesam.core.test.selenium.report.support.HtmlReportSupport;
import sk.seges.sesam.core.test.selenium.report.support.ScreenshotSupport;
import sk.seges.sesam.core.test.selenium.support.MailSupport;
import sk.seges.sesam.core.test.selenium.support.SeleniumSupport;
import sk.seges.sesam.core.test.webdriver.WebDriverActions;

public abstract class AbstractSeleniumTest extends BromineTest {

	protected MailSupport mailSupport;
	protected SeleniumSupport seleniumSupport;

	private String testName;
	protected Wait<WebDriver> wait;
	
	protected Actions actions;
	protected CoreSeleniumSettingsProvider settings;
	
	private ScreenshotSupport screenshotSupport;
	private HtmlReportSupport reportSupport;
	
	protected AbstractSeleniumTest() {
		setTestEnvironment(ensureSettings().getSeleniumSettings());
	}

	protected abstract CoreSeleniumSettingsProvider getSettings();

	@Override
	protected WebDriver createWebDriver(WebDriverFactory webDriverFactory, SeleniumSettings seleniumSettings) {
		WebDriver webDriver = super.createWebDriver(webDriverFactory, seleniumSettings);
		
		if (Boolean.TRUE.equals(ensureSettings().getReportSettings().getHtml().getSupport().getEnabled())) {
			this.reportSupport = new HtmlReportSupport(this.getClass(), ensureSettings().getReportSettings());
			webDriver = this.reportSupport.registerTo(new EventFiringWebDriver(webDriver));
		}
		
		if (Boolean.TRUE.equals(ensureSettings().getReportSettings().getScreenshot().getEnabled())) {
			this.screenshotSupport = new ScreenshotSupport(webDriver, ensureSettings().getReportSettings());
		}
		
		return webDriver;
	}

	protected CoreSeleniumSettingsProvider ensureSettings() {
		if (settings == null) {
			settings = getSettings();
		}
		
		return settings;
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
		
		if (this.screenshotSupport != null) {
			this.screenshotSupport.initialize();
		}
		
		if (this.reportSupport != null) {
			this.reportSupport.initialize();
		}
		
		start();
	}

	protected void start() {
		webDriver.get(testEnvironment.getTestURL() + testEnvironment.getTestURI());
	}
	
	@After
	public void tearDown() {

		if (this.screenshotSupport != null) {
			this.screenshotSupport.finish();
		}

		if (this.reportSupport != null) {
			this.reportSupport.finish();
		}

		super.tearDown();
	}
}