package sk.seges.sesam.core.test.selenium.report.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.model.EnvironmentInfo;
import sk.seges.sesam.core.test.selenium.report.model.api.TestResultCollector;
import sk.seges.sesam.core.test.selenium.report.printer.ReportPrinter;

public class ReportEventListener implements WebDriverEventListener {

	private final TestCaseResult testInfo;

	private ReportPrinter<TestCaseResult> reportPrinter;
	private List<TestResultCollector> webDriverEventListeners = new ArrayList<TestResultCollector>();
	private final ReportSettings reportSettings;
	private final WebDriver webDriver;
	private final EnvironmentInfo environmentInfo;

	private boolean processing = false;
	
	public ReportEventListener(Class<? extends AbstractSeleniumTest> testCase, ReportPrinter<TestCaseResult> reportPrinter, ReportSettings reportSettings, WebDriver webDriver, EnvironmentInfo environmentInfo, String testMethod) {
		this.reportPrinter = reportPrinter;
		this.testInfo = new TestCaseResult(testCase, testMethod);
		this.reportSettings = reportSettings;
		this.webDriver = webDriver;
		this.environmentInfo = environmentInfo;
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

	private CommandResult merge(List<CommandResult> commandResults) {
		CommandResult previousResult = null;
		
		if (testInfo.getCommandResults().size() > 0) {
			previousResult = testInfo.getCommandResults().get(testInfo.getCommandResults().size() - 1);
		}
		
		CommandResult result = new CommandResult(previousResult, reportSettings.getHtml().getLocale(), webDriver, environmentInfo);
		
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
}