package sk.seges.sesam.core.test.selenium.report.model.api;

import org.openqa.selenium.support.events.WebDriverEventListener;

import sk.seges.sesam.core.test.selenium.report.model.CommandResult;

public interface TestResultCollector extends WebDriverEventListener {

	void initialize();

	CommandResult getCommandResult();

	void finish();

}