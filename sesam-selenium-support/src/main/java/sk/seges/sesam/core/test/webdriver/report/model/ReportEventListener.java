package sk.seges.sesam.core.test.webdriver.report.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperation;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationState;
import sk.seges.sesam.core.test.webdriver.AbstractWebdriverTest;
import sk.seges.sesam.core.test.webdriver.model.EnvironmentInfo;
import sk.seges.sesam.core.test.webdriver.report.ActionsListener;
import sk.seges.sesam.core.test.webdriver.report.model.api.TestResultCollector;
import sk.seges.sesam.core.test.webdriver.report.printer.ReportPrinter;
import sk.seges.sesam.core.test.webdriver.support.event.AssertionEventListener;

public class ReportEventListener implements WebDriverEventListener, AssertionEventListener, ActionsListener {

	private final TestCaseResult testInfo;

	private ReportPrinter<TestCaseResult> reportPrinter;
	private List<TestResultCollector> webDriverEventListeners = new ArrayList<TestResultCollector>();
	private final ReportSettings reportSettings;
	private final WebDriver webDriver;
	private final EnvironmentInfo environmentInfo;

	private boolean processing = false;
	
	public ReportEventListener(Class<? extends AbstractWebdriverTest> testCase, ReportPrinter<TestCaseResult> reportPrinter, ReportSettings reportSettings, WebDriver webDriver, EnvironmentInfo environmentInfo) {
		this.reportPrinter = reportPrinter;
		this.testInfo = new TestCaseResult(testCase, reportSettings.getHtml().getIssueTracker());
		this.reportSettings = reportSettings;
		this.webDriver = webDriver;
		this.environmentInfo = environmentInfo;
	}

	public void setTestMethod(String testMethod) {
		this.testInfo.setTestMethod(testMethod);
	}
	
	public void addTestResultCollector(TestResultCollector testResultCollector) {
		webDriverEventListeners.add(testResultCollector);
	}
	
	public void initialize() {
		this.testInfo.startTest();
		this.reportPrinter.initialize(testInfo);

		for (TestResultCollector testInfoCollector: webDriverEventListeners) {
			testInfoCollector.initialize();
		}
	}
	
	public TestCaseResult getTestInfo() {
		return testInfo;
	}
	
	public void finish() {
		this.testInfo.endTest();

		for (TestResultCollector testInfoCollector: webDriverEventListeners) {
			testInfoCollector.finish();
		}

		this.reportPrinter.finish(testInfo);
	}

	private CommandResult getLastCommandResult() {
		CommandResult previousResult = null;
		
		if (testInfo.getCommandResults().size() > 0) {
			previousResult = testInfo.getCommandResults().get(testInfo.getCommandResults().size() - 1);
		}
		
		return previousResult;
	}
	
	private CommandResult merge(List<CommandResult> commandResults) {
		
		CommandResult result = new CommandResult(getLastCommandResult(), reportSettings.getHtml().getLocale(), webDriver, environmentInfo);
		
		for (CommandResult commandResult: commandResults) {
			if (commandResult.getOperation() != null) {
				result.setOperation(commandResult.getOperation());
			}
			if (commandResult.getParameters() != null) {
				result.setParameters(commandResult.getParameters());
			}
			if (commandResult.getResult() != null) {
				result.setResult(commandResult.getResult());
			}
			if (commandResult.isScreenshotUpdated()) {
				result.setScreenshotName(commandResult.getScreenshotName());
			}
			if (commandResult.getState() != null) {
				result.setState(commandResult.getState());
			}
			if (commandResult.getThrowable() != null) {
				result.setThrowable(commandResult.getThrowable());
			}
		}
		return result;
	}
	
	@Override
	public void afterChangeValueOf(WebElement webElement, WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterChangeValueOf(webElement, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterClickOn(WebElement webElement, WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterClickOn(webElement, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterFindBy(By by, WebElement webElement, WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterFindBy(by, webElement, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterNavigateBack(WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterNavigateBack(webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterNavigateForward(WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterNavigateForward(webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterNavigateTo(String url, WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterNavigateTo(url, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterScript(String script, WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterScript(script, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeChangeValueOf(webElement, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeClickOn(WebElement webElement, WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeClickOn(webElement, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeFindBy(by, webElement, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeNavigateBack(WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeNavigateBack(webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeNavigateForward(WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeNavigateForward(webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeNavigateTo(String url, WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeNavigateTo(url, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeScript(String script, WebDriver webDriver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeScript(script, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	private static final String SCREENSHOT_EXCEPTION = "screenshot";
	
	@Override
	public void onException(Throwable exception, WebDriver webDriver) {
		if (processing) {
			return;
		}
		
		if (exception.getMessage().contains(SCREENSHOT_EXCEPTION)) {
			//exception was thrown while making screenshot, so no handling
			return;
		}
		List<CommandResult> results = new LinkedList<CommandResult>();
		
		for (TestResultCollector testInfoCollector: webDriverEventListeners) {
			testInfoCollector.onException(exception, webDriver);
			CommandResult commandResult = testInfoCollector.getCommandResult();
			results.add(commandResult);
		}
		
		testInfo.getCommandResults().add(merge(results));
		reportPrinter.print(testInfo);
	}

	private CommandResult getBeforeCommandResult(SeleniumOperation operation, Object... params) {
		CommandResult commandResult = new CommandResult(getLastCommandResult(), reportSettings.getHtml().getLocale(), webDriver, environmentInfo);
		commandResult.setState(SeleniumOperationState.BEFORE);
		commandResult.setOperation(operation);
		commandResult.setResult(SeleniumOperationResult.NONE);
		commandResult.setParameters(params);
		return commandResult;
	}

	@Override
	public void onAssertion(Boolean result, Boolean statement1, ComparationType type, String comment) {
		processing = true;
		try {
			testInfo.getCommandResults().add(getBeforeCommandResult(SeleniumOperation.ASSERTION, comment));
			reportPrinter.print(testInfo);

			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.onAssertion(result, statement1, type, comment);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void onAssertion(Boolean result, Object statement1, Object statement2, ComparationType type, String comment) {
		processing = true;
		try {
			testInfo.getCommandResults().add(getBeforeCommandResult(SeleniumOperation.ASSERTION, comment + " ( Expecting: " + statement1 + ", found: " + statement2 + " )"));
			reportPrinter.print(testInfo);

			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.onAssertion(result, statement1, statement2, type, comment);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void onVerification(Boolean result, Boolean statement1, ComparationType type, String comment) {
		processing = true;
		try {
			
			testInfo.getCommandResults().add(getBeforeCommandResult(SeleniumOperation.VERIFICATION, comment));
			reportPrinter.print(testInfo);

			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.onVerification(result, statement1, type, comment);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void onVerification(Boolean result, Object statement1, Object statement2, ComparationType type, String comment) {
		processing = true;
		try {
			testInfo.getCommandResults().add(getBeforeCommandResult(SeleniumOperation.VERIFICATION, comment + " ( Expecting: " + statement1 + ", found: " + statement2 + " )"));
			reportPrinter.print(testInfo);

			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.onVerification(result, statement1, statement2, type, comment);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeClickAndHold(WebElement element, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeClickAndHold(element, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterClickAndHold(WebElement element, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterClickAndHold(element, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeDoubleClickOn(WebElement element, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeDoubleClickOn(element, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterDoubleClickOn(WebElement element, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterDoubleClickOn(element, webDriver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeKeyDown(Keys key, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeKeyDown(key, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterKeyDown(Keys key, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterKeyDown(key, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeKeyUp(Keys key, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeKeyUp(key, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterKeyUp(Keys key, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterKeyUp(key, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeSendKeys(CharSequence[] keysToSend, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeSendKeys(keysToSend, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterSendKeys(CharSequence[] keysToSend, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterSendKeys(keysToSend, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeButtonRelease(WebElement webElement, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeButtonRelease(webElement, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterButtonRelease(WebElement webElement, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterButtonRelease(webElement, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeMouseMove(WebElement element, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeMouseMove(element, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterMouseMove(WebElement element, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterMouseMove(element, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeMoveToOffset(WebElement element, int x, int y, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeMoveToOffset(element, x, y, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterMoveToOffset(WebElement element, int x, int y, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterMoveToOffset(element, x, y, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void beforeContextClick(WebElement element, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.beforeContextClick(element, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}

	@Override
	public void afterContextClick(WebElement element, WebDriver driver) {
		processing = true;
		try {
			List<CommandResult> results = new LinkedList<CommandResult>();
			
			for (TestResultCollector testInfoCollector: webDriverEventListeners) {
				testInfoCollector.afterContextClick(element, driver);
				CommandResult commandResult = testInfoCollector.getCommandResult();
				results.add(commandResult);
			}
			
			testInfo.getCommandResults().add(merge(results));
			reportPrinter.print(testInfo);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			processing = false;
		}
	}
}