package sk.seges.sesam.core.test.selenium.report;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings.ScreenshotSettings.AfterSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings.ScreenshotSettings.BeforeSettings;
import sk.seges.sesam.core.test.selenium.model.EnvironmentInfo;
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
	private final WebDriver webDriver;
	private final EnvironmentInfo environmentInfo;

	public ScreenshotsWebDriverEventListener(ReportSettings reportSettings, WebDriver webDriver, EnvironmentInfo environmentInfo) {
		this.screenshotSupport = new ScreenshotSupport(webDriver, reportSettings, environmentInfo);
		this.reportSettings = reportSettings;
		this.webDriver = webDriver;
		this.environmentInfo = environmentInfo;
	}

	@Override
	public CommandResult getCommandResult() {
		return commandResult;
	}

	private String getName(int screenshotIndex, SeleniumOperationState state, SeleniumOperation operation) {
		return (screenshotIndex < 10 ? "00" : screenshotIndex < 100 ? "0" : "") + (screenshotIndex++) + "_" + state.name().toLowerCase() + "_" + operation.name().toLowerCase();
	}

	private CommandResult getCommandResult(String name) {
		CommandResult commandResult = new CommandResult(getCommandResult(), reportSettings.getHtml().getLocale(), webDriver, environmentInfo);
		commandResult.setScreenshotName(name);
		return commandResult;
	}

	private boolean initialScreenshot = false;
	
	private void makeScreenshot(SeleniumOperationState state, SeleniumOperation operation) {
		
		if (!initialScreenshot) {
			screenshotSupport.makeScreenshot(DEFAULT_SCREENSHOT);
			initialScreenshot = true;
		}
		
		BeforeSettings before = reportSettings.getScreenshot().getBefore();
		if (state.equals(SeleniumOperationState.BEFORE)) {
			for (SeleniumOperation definedOperation: before.getValue()) {
				if (operation.equals(definedOperation)) {
					String name = getName(screenshotIndex++, state, operation);
					commandResult = getCommandResult(name);
					commandResult.setOperation(operation);
					commandResult.setState(state);
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
					commandResult.setOperation(operation);
					commandResult.setState(state);
					screenshotSupport.makeScreenshot(name);
					return;
				}
			}
		}
		
		commandResult = getCommandResult(null);
		commandResult.setOperation(operation);
		commandResult.setState(state);
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
	public void onException(Throwable throwable, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, commandResult.getOperation());
	}

	public static final String DEFAULT_SCREENSHOT = "0_initialize";
	public static final String FINAL_SCREENSHOT = "0_finalize";

	@Override
	public void initialize() {}

	@Override
	public void finish() {
		screenshotSupport.makeScreenshot(FINAL_SCREENSHOT);
	}
}