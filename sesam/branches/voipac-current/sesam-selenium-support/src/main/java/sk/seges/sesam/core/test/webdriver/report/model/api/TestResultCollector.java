package sk.seges.sesam.core.test.webdriver.report.model.api;

import org.openqa.selenium.support.events.WebDriverEventListener;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;
import sk.seges.sesam.core.test.webdriver.report.model.CommandResult;
import sk.seges.sesam.core.test.webdriver.support.event.AssertionEventListener;

public interface TestResultCollector extends WebDriverEventListener, AssertionEventListener, ActionsListener {

	void initialize();

	CommandResult getCommandResult();

	void finish();

}