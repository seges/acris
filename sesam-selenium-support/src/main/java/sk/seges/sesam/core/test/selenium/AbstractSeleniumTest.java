package sk.seges.sesam.core.test.selenium;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;

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
import bromine.brunit.BRUnit;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.HttpCommandProcessor;
import com.unitedinternet.portal.selenium.utils.logging.HtmlResultFormatter;
import com.unitedinternet.portal.selenium.utils.logging.LoggingCommandProcessor;
import com.unitedinternet.portal.selenium.utils.logging.LoggingDefaultSelenium;
import com.unitedinternet.portal.selenium.utils.logging.LoggingResultsFormatter;
import com.unitedinternet.portal.selenium.utils.logging.LoggingUtils;

public abstract class AbstractSeleniumTest extends BRUnit implements SeleniumConfigurator, MailSupport, SeleniumSupport {

	private static final String RESULT_FILE_ENCODING = "UTF-8";

	protected TestEnvironment testEnvironment;
	protected MailSettings mailEnvironment;
	protected ReportingSettings reportingSettings;
	protected CredentialsSettings credentialsSettings;

	protected SeleniumConfigurator seleniumConfigurator;
	protected MailSupport seleniumMailSupport;
	protected SeleniumSupport seleniumSupport;

	private static final String RESULT_PATH_PREFIX = "target" + File.separator;

	private LoggingCommandProcessor loggingProcessor;
	private BufferedWriter loggingWriter;

	protected AbstractSeleniumTest() {
		seleniumConfigurator = getSeleniumConfigurator();
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

	@Override
	public void start(String host, int port, String browser, String sitetotest, String test_id) {
		
		if (reportingSettings != null && reportingSettings.isProduceScreenshots() != null && reportingSettings.isProduceScreenshots() == true) {

			if (!new File(getScreenshotDirectory()).exists()) {
				new File(getScreenshotDirectory()).mkdirs();
			}

			String resultHtmlFileName = null;

			resultHtmlFileName = getResultDirectory() + "result" + LoggingUtils.timeStampForFileName() + ".html";

			loggingWriter = LoggingUtils.createWriter(resultHtmlFileName, RESULT_FILE_ENCODING, true);
			LoggingResultsFormatter htmlFormatter = new HtmlResultFormatter(loggingWriter, RESULT_FILE_ENCODING) {
				
			};

			htmlFormatter.setScreenShotBaseUri("file:///" + getScreenshotDirectory());
			htmlFormatter.setAutomaticScreenshotPath("file:///" + getScreenshotDirectory() + File.separator);

			loggingProcessor = new LoggingCommandProcessor(new HttpCommandProcessor(host, port, browser, sitetotest),
					htmlFormatter);
			loggingProcessor.setExcludedCommands(new String[] {});
		}

		super.start(host, port, browser, sitetotest, test_id);

		seleniumMailSupport = getMailSupport();
		seleniumSupport = getSeleniumSupport();
	}

	@Override
	protected DefaultSelenium createSelenium(String host, int port, String browser, String sitetotest) {
		if (loggingProcessor != null) {
			return new LoggingDefaultSelenium(loggingProcessor);
		}
		return super.createSelenium(host, port, browser, sitetotest);
	}

	protected SeleniumSupport getSeleniumSupport() {
		return new DefaultSeleniumSupport(selenium);
	}
	
	protected MailSupport getMailSupport() {
		return new DefaultMailSupport(mailEnvironment);
	}

	public SeleniumConfigurator getSeleniumConfigurator() {
		return new DefaultSeleniumConfigurator();
	}

	public void setTestEnvironment(TestEnvironment testEnvironment) {
		this.testEnvironment = testEnvironment;
	}

	public void setMailEnvironment(MailSettings mailEnvironment) {
		this.mailEnvironment = mailEnvironment;
	}

	@Override
	public String getRandomString(int length) {
		return seleniumSupport.getRandomString(length);
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

	public void runTests() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		setUp(testEnvironment.getBromineEnvironment().getBromineHost(), testEnvironment.getBromineEnvironment()
				.getBrominePort());
		start(testEnvironment.getSeleniumEnvironment().getSeleniumHost(), testEnvironment.getSeleniumEnvironment()
				.getSeleniumPort(), testEnvironment.getBrowser(), testEnvironment.getHost(), getClass().getSimpleName());
	}

	@After
	public void tearDown() throws Exception {
		if (loggingProcessor != null) {
//			selenium.captureScreenshot(getScreenshotDirectory() + File.separator + "result_" + LoggingUtils.timeStampForFileName() + ".png");
			selenium.captureEntirePageScreenshot(getScreenshotDirectory() + File.separator + "result_" + LoggingUtils.timeStampForFileName() + ".png", "background=#FFFFFF");
		}

		super.tearDown();

		try {
			if (null != loggingWriter) {
				loggingWriter.close();
			}
		} catch (IOException e) {
			// do nothing
		}
	}

	@Override
	public void verifyTrue(Boolean statement1) throws Exception {
		if (!statement1) {
			throw new RuntimeException("Test failed");
		}
	}

	@Override
	public void waitAndClick(String xpath) {
		seleniumSupport.waitAndClick(xpath);
	}

	@Override
	public void waitForMailNotPresent(String subject) throws InterruptedException {
		seleniumMailSupport.waitForMailNotPresent(subject);
	}

	@Override
	public String waitForMailPresent(String subject) throws InterruptedException {
		return seleniumMailSupport.waitForMailPresent(subject);
	}

	@Override
	public void waitForTextPresent(String xpath) {
		seleniumSupport.waitForTextPresent(xpath);
	}

	@Override
	public void waitForElementPresent(String xpath){
		seleniumSupport.waitForElementPresent(xpath);
	}

	@Override
	public void waitForAlertPresent() {
		seleniumSupport.waitForAlertPresent();
	}

	@Override
	public void waitForAlert(String pattern) {
		seleniumSupport.waitForAlert(pattern);
	}

	@Override
	public void fail(String message) {
		seleniumSupport.fail(message);
	}

	@Override
	public void waitForTextsPresent(String... xpaths) {
		seleniumSupport.waitForTextsPresent(xpaths);
	}

	@Override
	public void waitForElementsPresent(String... xpaths) {
		seleniumSupport.waitForElementsPresent(xpaths);
	}

	@Override
	public void clickOnElement(String xpath) {
		seleniumSupport.clickOnElement(xpath);
	}	
}