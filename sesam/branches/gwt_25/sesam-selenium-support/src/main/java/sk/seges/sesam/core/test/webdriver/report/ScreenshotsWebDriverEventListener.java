package sk.seges.sesam.core.test.webdriver.report;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings.ScreenshotSettings.AfterSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings.ScreenshotSettings.BeforeSettings;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperation;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationState;
import sk.seges.sesam.core.test.webdriver.AbstractWebdriverTest.ReportContext;
import sk.seges.sesam.core.test.webdriver.JunitAssertionDelegate.AssertionResult;
import sk.seges.sesam.core.test.webdriver.model.EnvironmentInfo;
import sk.seges.sesam.core.test.webdriver.report.model.CommandResult;
import sk.seges.sesam.core.test.webdriver.report.model.api.TestResultCollector;
import sk.seges.sesam.core.test.webdriver.report.support.ScreenshotSupport;

public class ScreenshotsWebDriverEventListener implements TestResultCollector {

	private final ScreenshotSupport screenshotSupport;
	private final ReportSettings reportSettings;
	private CommandResult commandResult;
	private int screenshotIndex = 1;
	private final WebDriver webDriver;
	private final EnvironmentInfo environmentInfo;
	private final ReportContext reportContext;
	
	public ScreenshotsWebDriverEventListener(ReportSettings reportSettings, ScreenshotSupport screenshotSupport, 
			ReportContext reportContext, WebDriver webDriver, EnvironmentInfo environmentInfo) {
		this.screenshotSupport = screenshotSupport;
		this.reportContext = reportContext;
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
				if (operation.equals(definedOperation) || operation.equals(SeleniumOperation.ALL)) {
					String name = getName(screenshotIndex++, state, operation);
					commandResult = reportContext.setCommandResult(getCommandResult(name));
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
				if (operation.equals(definedOperation) || operation.equals(SeleniumOperation.ALL)) {
					String name = getName(screenshotIndex++, state, operation);
					commandResult = reportContext.setCommandResult(getCommandResult(name));
					commandResult.setOperation(operation);
					commandResult.setState(state);
					screenshotSupport.makeScreenshot(name);
					return;
				}
			}
		}
		
		commandResult = reportContext.setCommandResult(getCommandResult(null));
		commandResult.setOperation(operation);
		commandResult.setState(state);
	}

	@Override
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

	@Override
	public void onAssertion(SeleniumOperationState state, AssertionResult result) {
		makeScreenshot(state, SeleniumOperation.ASSERTION);
	}

	@Override
	public void onFail(SeleniumOperationState state, String message) {
		makeScreenshot(state, SeleniumOperation.FAIL);
	}

	@Override
	public void beforeDoubleClickOn(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.DOUBLE_CLICK_ON);
	}

	@Override
	public void afterDoubleClickOn(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.DOUBLE_CLICK_ON);
	}

	@Override
	public void beforeKeyDown(Keys key, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.KEY_DOWN);
	}

	@Override
	public void afterKeyDown(Keys key, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.KEY_DOWN);
	}

	@Override
	public void beforeKeyUp(Keys key, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.KEY_DOWN);
	}

	@Override
	public void afterKeyUp(Keys key, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.KEY_UP);
	}

	@Override
	public void beforeClickAndHold(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.CLICK_AND_HOLD);
	}

	@Override
	public void afterClickAndHold(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.CLICK_AND_HOLD);
	}

	@Override
	public void beforeSendKeys(CharSequence[] keysToSend, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.SEND_KEYS);
	}

	@Override
	public void afterSendKeys(CharSequence[] keysToSend, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.SEND_KEYS);
	}

	public static final String DEFAULT_SCREENSHOT = "0_initialize";
	public static final String FINAL_SCREENSHOT = "0_finalize";

	@Override
	public void initialize() {}

	@Override
	public void finish() {
		screenshotSupport.makeScreenshot(FINAL_SCREENSHOT);
	}

	@Override
	public void beforeButtonRelease(WebElement webElement, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.BUTTON_RELEASE);
	}

	@Override
	public void afterButtonRelease(WebElement webElement, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.BUTTON_RELEASE);
	}

	@Override
	public void beforeMouseMove(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.MOUSE_MOVE);
	}

	@Override
	public void afterMouseMove(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.MOUSE_MOVE);
	}

	@Override
	public void beforeMoveToOffset(WebElement element, int x, int y, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.MOVE_TO_OFFSET);
	}

	@Override
	public void afterMoveToOffset(WebElement element, int x, int y, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.MOVE_TO_OFFSET);
	}

	@Override
	public void beforeContextClick(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.BEFORE, SeleniumOperation.CONTEXT_CLICK);
	}

	@Override
	public void afterContextClick(WebElement element, WebDriver driver) {
		makeScreenshot(SeleniumOperationState.AFTER, SeleniumOperation.CONTEXT_CLICK);
	}
}