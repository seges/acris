package sk.seges.sesam.core.test.selenium;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import sk.seges.sesam.core.test.bromine.BromineTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;
import sk.seges.sesam.core.test.selenium.configuration.model.CoreSeleniumSettingsProvider;
import sk.seges.sesam.core.test.selenium.support.MailSupport;
import sk.seges.sesam.core.test.selenium.support.SeleniumSupport;
import sk.seges.sesam.core.test.webdriver.WebDriverActions;

public abstract class AbstractSeleniumTest extends BromineTest {

//	private static final String RESULT_FILE_ENCODING = "UTF-8";

//	protected MailSettings mailSettings;
//	protected ReportingSettings reportingSettings;
//	protected CredentialsSettings credentialsSettings;
//
//	protected SeleniumConfigurer seleniumConfigurator;
	protected MailSupport mailSupport;
	protected SeleniumSupport seleniumSupport;

	private static final String RESULT_PATH_PREFIX = "target" + File.separator;

//	private LoggingCommandProcessor loggingProcessor;
//	private BufferedWriter loggingWriter;

	private String testName;
	protected Wait<WebDriver> wait;
	
	protected Actions actions;
	protected CoreSeleniumSettingsProvider settings;
	
	protected AbstractSeleniumTest() {
		setTestEnvironment(ensureSettings().getSeleniumSettings());
	}

	protected abstract CoreSeleniumSettingsProvider getSettings();
	
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
	
	protected String getResultDirectory() {
		String result = RESULT_PATH_PREFIX
				+ (ensureSettings().getReportSettings().getScreenshot().getDirectory() == null ? "report" : ensureSettings().getReportSettings().getScreenshot().getDirectory());

		result = new File(result).getAbsolutePath();
		result += File.separator;

		return result;
	}
	
	protected String getScreenshotDirectory() {

		String screenshotsDirectory = (ensureSettings().getReportSettings().getScreenshot().getDirectory() == null ? "screenshot"
				: ensureSettings().getReportSettings().getScreenshot().getDirectory());

		return getResultDirectory() + screenshotsDirectory;
	}
	
//	@Override
//	protected DefaultSelenium createSelenium(String host, int port, String browser, String sitetotest) {
//		if (loggingProcessor != null) {
//			return new LoggingDefaultSelenium(loggingProcessor);
//		}
//		return super.createSelenium(host, port, browser, sitetotest);
//	}

	protected MailSupport getMailSupport() {
		return new MailSupport(ensureSettings().getMailSettings());
	}
	
	protected SeleniumSupport getSeleniumSupport() {
		return new SeleniumSupport(webDriver);
	}

	@Before
	public void setUp() {
		if (ensureSettings().getReportSettings() != null && 
			ensureSettings().getReportSettings().getScreenshot().getEnabled() != null && 
			ensureSettings().getReportSettings().getScreenshot().getEnabled() == true) {

			if (!new File(getScreenshotDirectory()).exists()) {
				new File(getScreenshotDirectory()).mkdirs();
			}

//			String resultHtmlFileName = null;

//			resultHtmlFileName = getResultDirectory() + "result" + LoggingUtils.timeStampForFileName() + ".html";

//			loggingWriter = LoggingUtils.createWriter(resultHtmlFileName, RESULT_FILE_ENCODING, true);
//			LoggingResultsFormatter htmlFormatter = new HtmlResultFormatter(loggingWriter, RESULT_FILE_ENCODING) {
//				
//			};

//			htmlFormatter.setScreenShotBaseUri("file:///" + getScreenshotDirectory());
			//htmlFormatter.setAutomaticScreenshotPath("file:///" + getScreenshotDirectory() + File.separator);

//			loggingProcessor = new LoggingCommandProcessor(new HttpCommandProcessor(host, port, browser, sitetotest),
//					htmlFormatter);
//			loggingProcessor.setExcludedCommands(new String[] {});
		}

//		super.start(host, port, browser, sitetotest, test_id);

		start();
	}

	protected void start() {
		webDriver.get(testEnvironment.getTestURL() + testEnvironment.getTestURI());
	}

	@After
	public void tearDown() {
//		if (loggingProcessor != null) {
////			selenium.captureScreenshot(getScreenshotDirectory() + File.separator + "result_" + LoggingUtils.timeStampForFileName() + ".png");
//			selenium.captureEntirePageScreenshot(getScreenshotDirectory() + File.separator + "result_" + LoggingUtils.timeStampForFileName() + ".png", "background=#FFFFFF");
//		}

		super.tearDown();

//		try {
//			if (null != loggingWriter) {
//				loggingWriter.close();
//			}
//		} catch (IOException e) {
//			// do nothing
//		}
	}

}