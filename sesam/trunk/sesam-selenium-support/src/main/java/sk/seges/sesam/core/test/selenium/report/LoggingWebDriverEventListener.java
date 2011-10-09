package sk.seges.sesam.core.test.selenium.report;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperation;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationResult;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationState;
import sk.seges.sesam.core.test.selenium.report.model.TestResult;
import sk.seges.sesam.core.test.selenium.report.printer.ReportPrinter;

public class LoggingWebDriverEventListener implements WebDriverEventListener {

	private final TestResult testInfo;
	private final ReportPrinter reportPrinter;
	private final ReportSettings resportSettings;
	
	public LoggingWebDriverEventListener(Class<? extends AbstractSeleniumTest> testCase, ReportSettings resportSettings, ReportPrinter reportPrinter) {
		this.testInfo = new TestResult(testCase);
		this.reportPrinter = reportPrinter;
		this.resportSettings = resportSettings;
	}

	public void initialize() {
		this.testInfo.startTest();
		this.reportPrinter.initialize(resportSettings, testInfo);
	}
	
	public void finish() {
		this.testInfo.endTest();
		this.reportPrinter.finish(testInfo);
	}
	
	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.BEFORE, SeleniumOperation.NAVIGATE_TO, SeleniumOperationResult.NONE, url);
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.AFTER, SeleniumOperation.NAVIGATE_TO, SeleniumOperationResult.OK, url);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.BEFORE, SeleniumOperation.NAVIGATE_BACK, SeleniumOperationResult.NONE);
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.AFTER, SeleniumOperation.NAVIGATE_BACK, SeleniumOperationResult.OK);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.BEFORE, SeleniumOperation.NAVIGATE_FORWARD, SeleniumOperationResult.NONE);
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.AFTER, SeleniumOperation.NAVIGATE_FORWARD, SeleniumOperationResult.OK);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.BEFORE, SeleniumOperation.FIND_BY, SeleniumOperationResult.NONE, by, element);
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.AFTER, SeleniumOperation.FIND_BY, SeleniumOperationResult.OK, by, element);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.BEFORE, SeleniumOperation.CLICK_ON, SeleniumOperationResult.NONE, element);
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.AFTER, SeleniumOperation.CLICK_ON, SeleniumOperationResult.OK, element);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.BEFORE, SeleniumOperation.CHANGE_VALUE, SeleniumOperationResult.NONE, element);
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.AFTER, SeleniumOperation.CHANGE_VALUE, SeleniumOperationResult.OK, element);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.BEFORE, SeleniumOperation.RUN_SCRIPT, SeleniumOperationResult.NONE, script);
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.AFTER, SeleniumOperation.RUN_SCRIPT, SeleniumOperationResult.OK, script);
		reportPrinter.print(testInfo);
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		testInfo.addResult(SeleniumOperationState.AFTER, SeleniumOperationResult.FAILURE, throwable);
		reportPrinter.print(testInfo);
	}
}