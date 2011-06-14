package sk.seges.sesam.core.test.selenium;

import java.io.BufferedWriter;
import java.io.File;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import sk.seges.sesam.core.test.bromine.BromineTest;
import sk.seges.sesam.core.test.selenium.configuration.DefaultSeleniumConfigurator;
import sk.seges.sesam.core.test.selenium.configuration.api.CredentialsSettings;
import sk.seges.sesam.core.test.selenium.configuration.api.MailSettings;
import sk.seges.sesam.core.test.selenium.configuration.api.ReportingSettings;
import sk.seges.sesam.core.test.selenium.configuration.api.SeleniumConfigurator;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.properties.ConfigurationValue;
import sk.seges.sesam.core.test.selenium.support.DefaultMailSupport;
import sk.seges.sesam.core.test.selenium.support.DefaultSeleniumSupport;
import sk.seges.sesam.core.test.selenium.support.api.MailSupport;
import sk.seges.sesam.core.test.selenium.support.api.SeleniumSupport;
import sk.seges.sesam.core.test.webdriver.WebDriverActions;

public abstract class AbstractSeleniumTest extends BromineTest implements SeleniumConfigurator, MailSupport, SeleniumSupport {

	private static final String RESULT_FILE_ENCODING = "UTF-8";

	protected MailSettings mailSettings;
	protected ReportingSettings reportingSettings;
	protected CredentialsSettings credentialsSettings;

	protected SeleniumConfigurator seleniumConfigurator;
	protected MailSupport mailSupport;
	protected SeleniumSupport seleniumSupport;

	private static final String RESULT_PATH_PREFIX = "target" + File.separator;

//	private LoggingCommandProcessor loggingProcessor;
	private BufferedWriter loggingWriter;

	private String testName;
	protected Wait<WebDriver> wait;
	
	protected Actions actions;
	
	protected AbstractSeleniumTest() {
		seleniumConfigurator = getSeleniumConfigurator();
	}

	public void setTestEnvironment(TestEnvironment testEnvironment) {
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
				+ (reportingSettings.getResultDirectory() == null ? "report" : reportingSettings.getResultDirectory());

		result = new File(result).getAbsolutePath();
		result += File.separator;

		return result;
	}
	
	protected String getScreenshotDirectory() {

		String screenshotsDirectory = (reportingSettings.getScreenshotsDirectory() == null ? "screenshot"
				: reportingSettings.getScreenshotsDirectory());

		return getResultDirectory() + screenshotsDirectory;
	}
	
//	@Override
//	protected DefaultSelenium createSelenium(String host, int port, String browser, String sitetotest) {
//		if (loggingProcessor != null) {
//			return new LoggingDefaultSelenium(loggingProcessor);
//		}
//		return super.createSelenium(host, port, browser, sitetotest);
//	}

	protected SeleniumSupport getSeleniumSupport() {
		return new DefaultSeleniumSupport(webDriver);
	}
	
	protected MailSupport getMailSupport() {
		return new DefaultMailSupport(mailSettings);
	}

	public SeleniumConfigurator getSeleniumConfigurator() {
		return new DefaultSeleniumConfigurator();
	}

	public void setMailEnvironment(MailSettings mailEnvironment) {
		this.mailSettings = mailEnvironment;
	}

	@Override
	public String getRandomString(int length) {
		return seleniumSupport.getRandomString(length);
	}

	@Override
	public String getRandomEmail() {
		return seleniumSupport.getRandomEmail();
	}
	
	public void setReportingSettings(ReportingSettings reportingSettings) {
		this.reportingSettings = reportingSettings;
	}

	public void setCredentialsSettings(CredentialsSettings credentialsSettings) {
		this.credentialsSettings = credentialsSettings;
	}
	
	@Override
	public ConfigurationValue[] collectSystemProperties() {
		return seleniumConfigurator.collectSystemProperties();
	}

	@Override
	public TestEnvironment mergeTestConfiguration(TestEnvironment environment) {
		return seleniumConfigurator.mergeTestConfiguration(environment);
	}

	@Override
	public MailSettings mergeMailConfiguration(MailSettings mailEnvironment) {
		return seleniumConfigurator.mergeMailConfiguration(mailEnvironment);
	}

	@Before
	public void setUp() {
		if (reportingSettings != null && reportingSettings.isProduceScreenshots() != null && reportingSettings.isProduceScreenshots() == true) {

			if (!new File(getScreenshotDirectory()).exists()) {
				new File(getScreenshotDirectory()).mkdirs();
			}

			String resultHtmlFileName = null;

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
		webDriver.get(testEnvironment.getHost() + testEnvironment.getUri());
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

	@Override
	public void waitForMailNotPresent(String subject) {
		mailSupport.waitForMailNotPresent(subject);
	}

	@Override
	public String waitForMailPresent(String subject) {
		return mailSupport.waitForMailPresent(subject);
	}

	@Override
	public void fail(String message) {
		seleniumSupport.fail(message);
	}
	
	@Override
	public <T> boolean isSorted(Iterable<T> iterable, Comparator<T> comparator) {
		return seleniumSupport.isSorted(iterable, comparator);
	}	
}