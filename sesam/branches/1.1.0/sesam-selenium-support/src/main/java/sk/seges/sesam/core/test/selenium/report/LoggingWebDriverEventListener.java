package sk.seges.sesam.core.test.selenium.report;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperation;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationResult;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationState;
import sk.seges.sesam.core.test.selenium.report.model.TestInfo;
import sk.seges.sesam.core.test.selenium.report.printer.ReportPrinter;

public class LoggingWebDriverEventListener implements WebDriverEventListener {

	private final TestInfo testInfo;
	private final ReportPrinter reportPrinter;
	
	public LoggingWebDriverEventListener(Class<? extends AbstractSeleniumTest> testCase, ReportPrinter reportPrinter) {
		this.testInfo = new TestInfo(testCase);
		this.reportPrinter = reportPrinter;
	}

	public void initialize() {
		this.testInfo.startTest();
		this.reportPrinter.initialize(testInfo);
	}
	
	public void finish() {
		this.testInfo.endTest();
		this.reportPrinter.finish(testInfo);
	}
	
	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		testInfo.setState(SeleniumOperationState.BEFORE);
		testInfo.setOperation(SeleniumOperation.NAVIGATE_TO);
		testInfo.setResult(SeleniumOperationResult.NONE);
		testInfo.setParameters(url);
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		testInfo.setState(SeleniumOperationState.AFTER);
		testInfo.setOperation(SeleniumOperation.NAVIGATE_TO);
		testInfo.setResult(SeleniumOperationResult.OK);
		testInfo.setParameters(url);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		testInfo.setState(SeleniumOperationState.BEFORE);
		testInfo.setOperation(SeleniumOperation.NAVIGATE_BACK);
		testInfo.setResult(SeleniumOperationResult.NONE);
		testInfo.setParameters();
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		testInfo.setState(SeleniumOperationState.AFTER);
		testInfo.setOperation(SeleniumOperation.NAVIGATE_BACK);
		testInfo.setResult(SeleniumOperationResult.OK);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		testInfo.setState(SeleniumOperationState.BEFORE);
		testInfo.setOperation(SeleniumOperation.NAVIGATE_FORWARD);
		testInfo.setResult(SeleniumOperationResult.NONE);
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		testInfo.setState(SeleniumOperationState.AFTER);
		testInfo.setOperation(SeleniumOperation.NAVIGATE_FORWARD);
		testInfo.setResult(SeleniumOperationResult.OK);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		testInfo.setState(SeleniumOperationState.BEFORE);
		testInfo.setOperation(SeleniumOperation.FIND_BY);
		testInfo.setResult(SeleniumOperationResult.NONE);
		testInfo.setParameters(by, element);
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		testInfo.setState(SeleniumOperationState.AFTER);
		testInfo.setOperation(SeleniumOperation.FIND_BY);
		testInfo.setResult(SeleniumOperationResult.OK);
		testInfo.setParameters(by, element);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		testInfo.setState(SeleniumOperationState.BEFORE);
		testInfo.setOperation(SeleniumOperation.CLICK_ON);
		testInfo.setResult(SeleniumOperationResult.NONE);
		testInfo.setParameters(element);
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		testInfo.setState(SeleniumOperationState.AFTER);
		testInfo.setOperation(SeleniumOperation.CLICK_ON);
		testInfo.setResult(SeleniumOperationResult.OK);
		testInfo.setParameters(element);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		testInfo.setState(SeleniumOperationState.BEFORE);
		testInfo.setOperation(SeleniumOperation.CHANGE_VALUE);
		testInfo.setResult(SeleniumOperationResult.NONE);
		testInfo.setParameters(element);
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver) {
		testInfo.setState(SeleniumOperationState.AFTER);
		testInfo.setOperation(SeleniumOperation.CHANGE_VALUE);
		testInfo.setResult(SeleniumOperationResult.OK);
		testInfo.setParameters(element);
		reportPrinter.print(testInfo);
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		testInfo.setState(SeleniumOperationState.BEFORE);
		testInfo.setOperation(SeleniumOperation.RUN_SCRIPT);
		testInfo.setResult(SeleniumOperationResult.NONE);
		testInfo.setParameters(script);
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		testInfo.setState(SeleniumOperationState.AFTER);
		testInfo.setOperation(SeleniumOperation.RUN_SCRIPT);
		testInfo.setResult(SeleniumOperationResult.OK);
		testInfo.setParameters(script);
		reportPrinter.print(testInfo);
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		testInfo.setState(SeleniumOperationState.AFTER);
		testInfo.setThrowable(throwable);
		testInfo.setResult(SeleniumOperationResult.FAILURE);
		reportPrinter.print(testInfo);
	}
}