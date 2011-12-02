package sk.seges.sesam.core.test.selenium.report;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings.ScreenshotSettings.AfterSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings.ScreenshotSettings.BeforeSettings;
import sk.seges.sesam.core.test.selenium.report.model.CommandResult;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperation;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationState;
import sk.seges.sesam.core.test.selenium.report.model.api.TestResultCollector;
import sk.seges.sesam.core.test.selenium.report.support.ScreenshotSupport;

public class ScreenshotsWebDriverEventListener implements TestResultCollector {

	private final ScreenshotSupport screenshotSupport;
	private final ReportSettings reportSettings;
	private CommandResult commandResult;
	private int screenshotIndex = 1;

	public ScreenshotsWebDriverEventListener(ScreenshotSupport screenshotSupport, ReportSettings reportSettings) {
		this.screenshotSupport = screenshotSupport;
		this.reportSettings = reportSettings;
	}

	@Override
	public CommandResult getCommandResult() {
		return commandResult;
	}

	private String getName(int screenshotIndex, SeleniumOperationState state, SeleniumOperation operation) {
		return (screenshotIndex < 10 ? "0" : "") + (screenshotIndex++) + "_" + state.name().toLowerCase() + "_" + operation.name().toLowerCase();
	}

	private CommandResult getCommandResult(String name) {
		CommandResult commandResult = new CommandResult();
		commandResult.setScreenshotName(name);
		return commandResult;
	}
	
	private void makeScreenshot(SeleniumOperationState state, SeleniumOperation operation) {
		BeforeSettings before = reportSettings.getScreenshot().getBefore();
		if (state.equals(SeleniumOperationState.BEFORE)) {
			for (SeleniumOperation definedOperation: before.getValue()) {
				if (operation.equals(definedOperation)) {
					String name = getName(screenshotIndex++, state, operation);
					commandResult = getCommandResult(name);
					screenshotSupport.makeScreenshot(name);
					return;
				}
			}
		}
		AfterSettings after = reportSettings.getScreenshot().getAfter();
		if (state.equals(SeleniumOperationState.AFTER)) {
			for (SeleniumOperation definedOperation: after.getValue()) {
				if (operation.equals(definedOperation)) {
					String name = getName(screenshotIndex++, state, operation);
					commandResult = getCommandResult(name);
					screenshotSupport.makeScreenshot(name);
					return;
				}
			}
		}
		
		commandResult = getCommandResult(null);
	}

	public void beforeNavigateTo(String url, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.NAVIGATE_TO);
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.NAVIGATE_TO);
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.NAVIGATE_BACK);
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.NAVIGATE_BACK);
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.NAVIGATE_FORWARD);
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.NAVIGATE_FORWARD);
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.FIND_BY);
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.FIND_BY);
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.CLICK_ON);
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.CLICK_ON);
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.CHANGE_VALUE);
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.CHANGE_VALUE);
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.RUN_SCRIPT);
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.RUN_SCRIPT);
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {}

	@Override
	public void initialize() {}

	@Override
	public void finish() {
		screenshotSupport.makeScreenshot();
	}
}