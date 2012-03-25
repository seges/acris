package sk.seges.sesam.core.test.webdriver.report;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperation;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationState;
import sk.seges.sesam.core.test.webdriver.model.EnvironmentInfo;
import sk.seges.sesam.core.test.webdriver.model.ValueChangeParameter;
import sk.seges.sesam.core.test.webdriver.report.model.CommandResult;
import sk.seges.sesam.core.test.webdriver.report.model.SeleniumOperationResult;
import sk.seges.sesam.core.test.webdriver.report.model.api.TestResultCollector;

public class LoggingWebDriverEventListener implements TestResultCollector {

	private CommandResult commandResult = null;

	private final ReportSettings reportSettings;
	private final WebDriver webDriver;
	private final EnvironmentInfo environmentInfo;
	
	public LoggingWebDriverEventListener(ReportSettings reportSettings, WebDriver webDriver, EnvironmentInfo environmentInfo) {
		this.reportSettings = reportSettings;
		this.webDriver = webDriver;
		this.environmentInfo = environmentInfo;
	}
	
	@Override
	public CommandResult getCommandResult() {
		return commandResult;
	}
	
	private CommandResult getCommandResult(SeleniumOperationState state, SeleniumOperationResult result, Throwable throwable) {
		CommandResult commandResult = new CommandResult(getCommandResult(), reportSettings.getHtml().getLocale(), webDriver, environmentInfo);
		commandResult.setState(state);
		commandResult.setResult(result);
		commandResult.setThrowable(throwable);
		return commandResult;
	}
	
	private CommandResult getCommandResult(SeleniumOperationState state, SeleniumOperation operation, SeleniumOperationResult result, Object... params) {
		CommandResult commandResult = new CommandResult(getCommandResult(), reportSettings.getHtml().getLocale(), webDriver, environmentInfo);
		commandResult.setState(state);
		commandResult.setOperation(operation);
		commandResult.setResult(result);
		if (operation.equals(SeleniumOperationState.AFTER) && params.length == 0) {
			commandResult.setParameters(commandResult.getParameters());
		} else {
			commandResult.setParameters(params);
		}
		return commandResult;
	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.NAVIGATE_TO, SeleniumOperationResult.NONE, url);
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.NAVIGATE_TO, SeleniumOperationResult.OK, url);
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.NAVIGATE_BACK, SeleniumOperationResult.NONE);
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.NAVIGATE_BACK, SeleniumOperationResult.OK);
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.NAVIGATE_FORWARD, SeleniumOperationResult.NONE);
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.NAVIGATE_FORWARD, SeleniumOperationResult.OK);
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.FIND_BY, SeleniumOperationResult.NONE, by, element);
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.FIND_BY, SeleniumOperationResult.OK, by);
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.CLICK_ON, SeleniumOperationResult.NONE, element);
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.CLICK_ON, SeleniumOperationResult.OK);
	}

	@Override
	public void beforeDoubleClickOn(WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.DOUBLE_CLICK_ON, SeleniumOperationResult.NONE, element);
	}

	@Override
	public void afterDoubleClickOn(WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.DOUBLE_CLICK_ON, SeleniumOperationResult.OK);
	}

	@Override
	public void beforeKeyDown(Keys key, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.KEY_DOWN, SeleniumOperationResult.NONE, key);
	}

	@Override
	public void afterKeyDown(Keys key, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.KEY_DOWN, SeleniumOperationResult.OK, key);
	}

	@Override
	public void beforeKeyUp(Keys key, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.KEY_UP, SeleniumOperationResult.NONE, key);
	}

	@Override
	public void afterKeyUp(Keys key, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.KEY_UP, SeleniumOperationResult.OK, key);
	}

	@Override
	public void beforeSendKeys(CharSequence[] keysToSend, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.SEND_KEYS, SeleniumOperationResult.NONE, (Object[])keysToSend);
	}

	@Override
	public void afterSendKeys(CharSequence[] keysToSend, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.SEND_KEYS, SeleniumOperationResult.OK, (Object[])keysToSend);
	}

	@Override
	public void beforeClickAndHold(WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.CLICK_AND_HOLD, SeleniumOperationResult.NONE, element);
	}
	
	@Override
	public void afterClickAndHold(WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.CLICK_AND_HOLD, SeleniumOperationResult.OK, element);
	}

	@Override
	public void beforeButtonRelease(WebElement webElement, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.BUTTON_RELEASE, SeleniumOperationResult.NONE, webElement);
	}

	@Override
	public void afterButtonRelease(WebElement webElement, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.BUTTON_RELEASE, SeleniumOperationResult.OK, webElement);
	}

	@Override
	public void beforeMouseMove(WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.MOUSE_MOVE, SeleniumOperationResult.NONE, element);
	}

	@Override
	public void afterMouseMove(WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.MOUSE_MOVE, SeleniumOperationResult.OK, element);
	}

	@Override
	public void beforeMoveToOffset(WebElement element, int x, int y, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.MOVE_TO_OFFSET, SeleniumOperationResult.NONE, element, x, y);
	}

	@Override
	public void afterMoveToOffset(WebElement element, int x, int y, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.MOVE_TO_OFFSET, SeleniumOperationResult.OK, element, x, y);
	}

	@Override
	public void beforeContextClick(WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.CONTEXT_CLICK, SeleniumOperationResult.NONE, element);
	}

	@Override
	public void afterContextClick(WebElement element, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.CONTEXT_CLICK, SeleniumOperationResult.OK, element);
	}

	private static final String VALUE = "value";
	
	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		ValueChangeParameter valueChangeParameter = new ValueChangeParameter(element.getAttribute(VALUE));
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.CHANGE_VALUE, SeleniumOperationResult.NONE, element, valueChangeParameter);
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver) {
		Object[] parameters = commandResult.getParameters();
		for (Object parameter: parameters) {
			if (parameter instanceof ValueChangeParameter) {
				((ValueChangeParameter)parameter).setTextAfterChange(element.getAttribute(VALUE));
			}
		}
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.CHANGE_VALUE, SeleniumOperationResult.OK);
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.BEFORE, SeleniumOperation.RUN_SCRIPT, SeleniumOperationResult.NONE, script);
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.RUN_SCRIPT, SeleniumOperationResult.OK, script);
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperationResult.FAILURE, throwable);
	}

	@Override
	public void onAssertion(Boolean result, Boolean statement1, ComparationType type, String comment) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.ASSERTION, 
				result == true ? SeleniumOperationResult.OK : SeleniumOperationResult.FAILURE);
	}

	@Override
	public void onAssertion(Boolean result, String statement1, String statement2, ComparationType type, String comment) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.ASSERTION, 
				result == true ? SeleniumOperationResult.OK : SeleniumOperationResult.FAILURE);
	}

	@Override
	public void onVerification(Boolean result, Boolean statement1, ComparationType type, String comment) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.VERIFICATION, 
				result == true ? SeleniumOperationResult.OK : SeleniumOperationResult.FAILURE);
	}

	@Override
	public void onVerification(Boolean result, String statement1, String statement2, ComparationType type, String comment) {
		commandResult = getCommandResult(SeleniumOperationState.AFTER, SeleniumOperation.VERIFICATION, 
				result == true ? SeleniumOperationResult.OK : SeleniumOperationResult.FAILURE);
	}

	@Override
	public void initialize() {}

	@Override
	public void finish() {}
}