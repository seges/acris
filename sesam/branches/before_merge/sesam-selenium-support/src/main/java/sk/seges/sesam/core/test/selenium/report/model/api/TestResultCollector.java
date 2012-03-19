package sk.seges.sesam.core.test.selenium.report.model.api;

import org.openqa.selenium.support.events.WebDriverEventListener;

import sk.seges.sesam.core.test.selenium.report.model.CommandResult;
import sk.seges.sesam.core.test.selenium.support.event.AssertionEventListener;

public interface TestResultCollector extends WebDriverEventListener, AssertionEventListener {

	void initialize();

	CommandResult getCommandResult();

	void finish();

}